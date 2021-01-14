package com.scfsoft.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.scfsoft.sdk.common.api.dto.StandardPagination;
import com.scfsoft.sdk.common.dto.Pagination;
import com.scfsoft.sdk.common.dto.TreeNode;
import com.scfsoft.system.dao.jpa.SysOrgDAO;
import com.scfsoft.system.dao.jpa.SysRoleResrDao;
import com.scfsoft.system.dao.jpa.SysUserRoleDAO;
import com.scfsoft.system.dao.mybatis.OrgMapper;
import com.scfsoft.system.dao.mybatis.UserMapper;
import com.scfsoft.system.dao.mybatis.UserRoleMapper;
import com.scfsoft.system.dto.OrgDto;
import com.scfsoft.system.dto.SysUserDto;
import com.scfsoft.system.dto.UserOrg;
import com.scfsoft.system.dto.UserSearchParam;
import com.scfsoft.system.entity.SysOrg;
import com.scfsoft.system.entity.SysUserRole;
import com.scfsoft.system.entity.jpa.SysRoleResr;
import com.scfsoft.system.entity.jpa.SysUserEntity;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysCommonService {

    private final UserRoleMapper userRoleMapper;

    private final SysRoleResrDao sysRoleResrDao;

    private final UserMapper userMapper;

    private final SysOrgDAO orgDAO;

    private final SysUserRoleDAO sysUserRoleDAO;

    private final OrgMapper orgMapper;

    public String getUserRole(String userId) {

        return userRoleMapper.getUserRole(userId);
    }

    public List<SysRoleResr> getRoleResr(String roleId) {

        return sysRoleResrDao.queryByRoleId(roleId);
    }

    public void saveBl(String userId, String businessLine) {
        userRoleMapper.saveBl(userId, businessLine);
    }

    public List<SysUserEntity> getBl(String userCode, StandardPagination page) {

        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<SysUserEntity> bl = userRoleMapper.getBl(userCode);
        PageInfo<SysUserEntity> pageInfo = new PageInfo<>(bl);
        page.setTotalPage(pageInfo.getPages());
        page.setTotalRecord((int) pageInfo.getTotal());
        return bl;
    }

    public UserOrg selectOrgByUser(String userId){
        return userRoleMapper.selectOrgByUser(userId);
    };

    public List<SysUserDto> getAllUsers(UserSearchParam request) {
        return userMapper.getSysAllUsers(request);
    }

    @Cacheable(cacheNames = "user-info", key = "#sid", unless = "#result eq null")
    public SysUserDto getUserInfo(String sid) {
        return userMapper.getUserInfo(sid);
    }

    public List<SysUserDto> fetchSysUsers(UserSearchParam param, Pagination page) {
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());

        List<SysUserDto> sysUserList =  userMapper.fetchUsersByPage(param);
        PageInfo<SysUserDto> pageInfo = new PageInfo<>(sysUserList);
        page.setTotalPage(pageInfo.getPageNum());
        page.setTotalRecord((int) pageInfo.getTotal());
        return sysUserList;
    }

    @CacheEvict(cacheNames = "user-info", key = "#userDto.sid")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveOrUpdUser(SysUserDto userDto) {
        // 查询用户是否存在
        if (getUserInfo(userDto.getSid()) == null) {
            userMapper.saveUserInfo(userDto);
        } else {
            userMapper.updateUser(userDto);
        }

        // 保存用户角色信息
        String role = userDto.getRoles();
        if (StringUtils.isNotEmpty(role)) {
            userRoleMapper.deleteRoleByUserId(userDto.getSid());
            SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(role);
            userRole.setUserId(userDto.getSid());
            sysUserRoleDAO.save(userRole);
        }
    }

    @Cacheable(cacheNames = "org-info", key = "#orgId", unless = "#result eq null")
    public String getOrgName(String orgId) {
        Optional<SysOrg> org = orgDAO.findById(orgId);
        if (org != null && org.isPresent()) {
            return org.get().getOrgNameShort();
        }
        return null;
    }

    public List<OrgDto> getOrgTree(String busiScene) {
        List<OrgDto> all = Lists.newArrayList();
        // 每个层级加入全部选项
        if (StringUtils.isNotEmpty(busiScene) && busiScene.equals("SQA")) {
            OrgDto org = new OrgDto();
            org.setId("320000000");
            org.setOrgName("全部二级行");
            all.add(org);
        }
        List<OrgDto> orgList = orgMapper.getOrgTree();
        all.addAll(orgList);
        return getTreeRoot(all, busiScene);
    }

    public static <T extends TreeNode> List<T> getTreeRoot(List<T> list, String busiScene) {
        List<T> treeRoot = list.stream().filter(tree -> StringUtils.isBlank(tree.getPid())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(busiScene) && busiScene.equals("SQA")) {
            treeRoot.forEach(tree -> getOrgTree(list, tree));
        } else {
            treeRoot.forEach(tree -> getTree(list, tree));
        }
        return treeRoot;
    }

    private static <T extends TreeNode> T getTree(List<T> list, T t) {
        List<T> children = list.stream().filter(tree -> t.getId().equals(tree.getPid())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(child -> t.addChild(getTree(list, child)));
        } else {
            t.setChildren(null);
        }
        return t;
    }

    private static <T extends TreeNode> T getOrgTree(List<T> list, T t) {
        List<T> children = list.stream().filter(tree -> t.getId().equals(tree.getPid())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(children)) {
            OrgDto org = new OrgDto();
            org.setId(t.getId());
            org.setOrgName("全部");
            org.setChildren(null);
            t.addChild(org);
            children.forEach(child -> t.addChild(getOrgTree(list, child)));
        } else {
            t.setChildren(null);
        }
        return t;
    }

}
