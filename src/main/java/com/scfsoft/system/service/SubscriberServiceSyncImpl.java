package com.scfsoft.system.service;

import com.google.common.collect.Lists;
import com.scfsoft.sdk.common.api.enums.StandardSubscriberParam;
import com.scfsoft.sdk.common.dto.*;
import com.scfsoft.sdk.common.utils.ReflectionUtils;
import com.scfsoft.sdk.common.utils.SpringUtils;
import com.scfsoft.sdk.common.utils.ThreadContextUtils;
import com.scfsoft.sdk.web.utils.HttpContextUtils;
import com.scfsoft.system.api.dto.Role;
import com.scfsoft.system.api.dto.Subscriber;
import com.scfsoft.system.api.dto.SubscriberSearchParam;
import com.scfsoft.system.api.service.SubscriberService;
import com.scfsoft.system.consts.SystemConsts;
import com.scfsoft.system.dao.jpa.SysOrgDAO;
import com.scfsoft.system.dao.jpa.SysUserParamDAO;
import com.scfsoft.system.dao.jpa.SysUserRoleDAO;
import com.scfsoft.system.dao.mybatis.UserMapper;
import com.scfsoft.system.entity.SysOrg;
import com.scfsoft.system.entity.SysUserParam;
import com.scfsoft.system.entity.SysUserRole;
import com.scfsoft.system.enums.SubscriberParam;
import com.scfsoft.system.token.TokenPayload;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lukejia
 * @date 2020/3/11 16:27
 */
@RequiredArgsConstructor
@Service
public class SubscriberServiceSyncImpl implements SubscriberService {
    private final PortalServiceInterface portalService;

    private final SysUserRoleDAO sysUserRoleDao;

    private final SysUserParamDAO sysUserParamDao;

    private final UserService userService;

    private final RoleService roleService;

    private final SysOrgDAO sysOrgDao;

    private final UserMapper userMapper;

    private static final String ORG_NAME = "ORG_NAME";
    private static final String LAST_VISITED = "LAST_VISITED";
    private static final String IP = "IP";

    @Override
    public String getSource() {
        return "PORTAL接口";
    }

    @Override
    public Subscriber getSubscriberByName(String username) {
        return this.getUserInfo(null, username);
    }

    @Override
    public Subscriber getSubscriberBySid(String sid) {
        return this.getUserInfo(sid, null);
    }

    @Override
    public Subscriber verifySubscriber(String username, String password) {
        return null;
    }

    @Override
    public <T> T getSubscriberParam(String sid, StandardSubscriberParam subscriberParam, Class<T> clazz) {
        SysUserParam sysUserParam = sysUserParamDao.findOneUserParam(sid, subscriberParam.getKey()).orElse(new SysUserParam());
        return ReflectionUtils.cast(sysUserParam.getParamValue(), clazz);
    }

    @Override
    public Subscriber saveSubscriber(Subscriber subscriber) {
        // 保存用户参数
        userService.saveUserParam(subscriber.getSid(), SubscriberParam.MOBILE, subscriber.getParam(SubscriberParam.MOBILE, String.class));
        userService.saveUserParam(subscriber.getSid(), SubscriberParam.EMAIL, subscriber.getParam(SubscriberParam.EMAIL, String.class));
        // 保存用户机构
        userService.saveUserParam(subscriber.getSid(), SubscriberParam.ORG_ID, subscriber.getParam(SubscriberParam.ORG_ID, String.class));
        // 保存用户角色
        this.saveUserRoles(subscriber.getSid(), subscriber.getRoles());
        return subscriber;
    }

    @Override
    public void dropSubscriber(String sid) {

    }

    @Override
    public List<Subscriber> getSubscribers(SubscriberSearchParam searchParam, Pagination page) {
        GridRequest<SubscriberSearchParam> request = new GridRequest<>();
        request.getData().setSearchParam(searchParam);
        request.getData().setPage(page);
        GridResponse<Subscriber> response = portalService.getSubscribers(request);
        return response.getData().getDatalist();
    }

    @Override
    public List<Subscriber> findRoleCandidates(String roleId, String search) {
        return this.getCandidates(search, roleId, null);
    }

    @Override
    public List<Subscriber> findOrgCandidates(String orgId, String search) {
        return this.getCandidates(search, null, orgId);
    }

    @Override
    public void saveParams(String sid, Map<String, Object> params) {
        Request<Map<String, Object>> request = new Request<>();
        request.setData(params);
        portalService.saveParam(request);
    }

    private List<Subscriber> getCandidates(String search, String roleId, String orgId) {
        SubscriberSearchParam searchParam = new SubscriberSearchParam();
        searchParam.setSearch(search);
        Pagination page = new Pagination();
        page.setPageNumber(1);
        page.setPageSize(20);
        List<Subscriber> subscribers = this.getSubscribers(searchParam, page);
        List<String> list = subscribers.parallelStream().map(Subscriber::getSid).collect(Collectors.toList());
        List<String> temp = StringUtils.isNotBlank(roleId) ? userMapper.getUserByRoles(roleId, list) : userMapper.getUserByOrgs(orgId, list);
        return subscribers.parallelStream().filter(subscriber -> temp.contains(subscriber.getSid())).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "user-info", key = "#sid", unless = "#result eq null")
    public String getUserInfo(String sid) {
        Subscriber subscriber = this.getSubscriberService().getSubscriberBySid(sid);
        String userName = "";
        if (subscriber != null) {
            userName = subscriber.getNickname() != null ? subscriber.getNickname() : subscriber.getUsername();
        }
        return userName;
    }

    private SubscriberService getSubscriberService() {
        return SpringUtils.getBean(SubscriberServiceProxyImpl.class);
    }

    private Subscriber getUserInfo(String sid, String username) {
        // 1.通过接口获取用户信息
        Response<Subscriber> response = portalService.getSubscriber(sid, username);
        Subscriber subscriber = response.getData();
        String id = subscriber.getSid();
        // 2.获取用户机构覆盖
        String orgId = this.getSubscriberParam(id, SubscriberParam.ORG_ID, String.class);
        if (StringUtils.isNotBlank(orgId)) {
            subscriber.getParams().put(SubscriberParam.ORG_ID.getKey(), orgId);
        } else {
            orgId = (String) subscriber.getParams().get(SubscriberParam.ORG_ID.getKey());
        }
        String orgName = sysOrgDao.findById(orgId).orElse(new SysOrg()).getOrgName();
        subscriber.getParams().put(ORG_NAME, orgName);
        // 3.获取用户角色
        List<SysUserRole> roles = sysUserRoleDao.findAll(id);
        // 4.获取权限合并
        roles.forEach(sysUserRole -> {
            Role role = roleService.getRole(sysUserRole.getRoleId());
            subscriber.getRoles().add(role);
            subscriber.getPerms().addAll(role.getPerms());
            subscriber.getPerms().addAll(role.getMenus());
        });
        TokenPayload tokenPayload = (TokenPayload) ThreadContextUtils.get(SystemConsts.THREAD_KEY_TOKEN_PAYLOAD);
        subscriber.getPerms().addAll(Stream.of(tokenPayload.getPerms()).collect(Collectors.toList()));
        // 5.获取用户最后登录时间和IP
        subscriber.getParams().put(LAST_VISITED, tokenPayload.getIssueTime());
        subscriber.getParams().put(IP, HttpContextUtils.getIp());
        return subscriber;
    }

    private void saveUserRoles(String sid, List<Role> roles) {
        List<SysUserRole> sysUserRoles = Lists.newArrayList();
        roles.forEach(role -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sid);
            sysUserRole.setRoleId(role.getRoleId());
            sysUserRoles.add(sysUserRole);
        });
        List<SysUserRole> sysUserRoleList = sysUserRoleDao.findAll(sid);
        sysUserRoleDao.deleteAll(sysUserRoleList);
        sysUserRoleDao.saveAll(sysUserRoles);
    }
}
