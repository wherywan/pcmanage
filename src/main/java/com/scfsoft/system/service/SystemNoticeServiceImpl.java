package com.scfsoft.system.service;

import com.scfsoft.system.dao.jpa.SysNoticeDao;
import com.scfsoft.system.dao.jpa.SysNoticePublishDao;
import com.scfsoft.system.dao.mybatis.SystemNoticeMapper;
import com.scfsoft.system.dto.MultiSystemNotice;
import com.scfsoft.system.dto.NoticeType;
import com.scfsoft.system.dto.SystemNotice;
import com.scfsoft.system.dto.SystemNoticeSearchParam;
import com.scfsoft.system.dao.jpa.SysOrgDAO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.scfsoft.sdk.common.dto.Pagination;
import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.sdk.common.utils.IDUtils;
import com.scfsoft.sdk.common.utils.StringUtils;
import com.scfsoft.system.entity.SysNotice;
import com.scfsoft.system.entity.SysNoticePublish;
import com.scfsoft.system.entity.SysOrg;
import com.scfsoft.system.enums.SystemNoticeScope;
import com.scfsoft.system.enums.SystemNoticeType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author guojingyu
 */
@AllArgsConstructor
@Service
public class SystemNoticeServiceImpl {

    private final SysNoticeDao sysNoticeDao;
    private final SysNoticePublishDao sysNoticePublishDao;
    private final SystemNoticeMapper systemNoticeMapper;

    private final SysOrgDAO sysOrgDAO;

    /**
     * 获取所有的机构列表，数组的形式sysNoticePublishDB
     * @return
     */
    public List<SysOrg> findAllOrg(){
        List<SysOrg> sysOrgList = sysOrgDAO.findAll();
        return sysOrgList;
    }


    public List<SystemNotice> getSystemNotices(String scope, String targetId) {
        return null;
    }

    /**
     * 筛选系统公告信息-管理端
     * @param searchParam
     * @param page
     * @return
     */

    public List<SystemNotice> getSystemNoticesByPage(SystemNoticeSearchParam searchParam, Pagination page) {
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<SystemNotice> systemNotices = systemNoticeMapper.findSystemNoticeByPage(searchParam);
        PageInfo<SystemNotice> pageInfo = new PageInfo<>(systemNotices);
        page.setTotalPage(pageInfo.getPages());
        page.setTotalRecord((int) pageInfo.getTotal());
        return systemNotices;
    }

    /**
     * 添加或更新系统公告信息
     * @param multiSystemNotice
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public MultiSystemNotice saveMultiSystemNotice(MultiSystemNotice multiSystemNotice) {

        String systemNoticeId = multiSystemNotice.getId();
        if (StringUtils.isEmpty(systemNoticeId)) {
            systemNoticeId = IDUtils.getShortUuid();
            multiSystemNotice.setId(systemNoticeId);
        }

        SysNotice sysNotice = new SysNotice();
        //更新公告实体
        sysNotice.setId(systemNoticeId);
        sysNotice.setNoticeType(multiSystemNotice.getNoticeType());
        sysNotice.setTitle(multiSystemNotice.getTitle());
        sysNotice.setContent(multiSystemNotice.getContent());
        sysNotice.setPublishTime(multiSystemNotice.getPublishTime());
        sysNotice.setIsVisible(multiSystemNotice.getIsVisible());
        sysNotice = sysNoticeDao.save(sysNotice);
        //更新公告发布实体
        List<SysNoticePublish> sysNoticePublishList = new ArrayList<>();
        List<String> targetIds = multiSystemNotice.getTargetId();
        for(int i = 0; i<targetIds.size(); i++){
            SysNoticePublish sysNoticePublish = new SysNoticePublish();
            sysNoticePublish.setNoticeId(sysNotice.getId());
            sysNoticePublish.setId(IDUtils.getShortUuid());
            sysNoticePublish.setScope(multiSystemNotice.getScope());
            sysNoticePublish.setTargetId(targetIds.get(i));
            sysNoticePublishList.add(sysNoticePublish);
        }

        List<SysNoticePublish> sysNoticePublishDb = sysNoticePublishDao.findByNoticeId(systemNoticeId);
        if(sysNoticePublishDb.size() > 0){
            sysNoticePublishDao.deleteByNoticeId(sysNotice.getId());
            sysNoticePublishDao.flush();//todo:比对后删除？
        }
        sysNoticePublishDao.saveAll(sysNoticePublishList);
        //更新返回内容
        multiSystemNotice.setCreatedBy(sysNotice.getCreateUser());
        multiSystemNotice.setCreatedTs(sysNotice.getCreateTime());
        multiSystemNotice.setLastUpdBy(sysNotice.getUpdateUser());
        multiSystemNotice.setLastUpdTs(sysNotice.getUpdateTime());
        return multiSystemNotice;
    }

    /**
     * 删除系统公告信息
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void dropSystemNotice(String sysNoticeId) {
        sysNoticeDao.deleteById(sysNoticeId);
        sysNoticePublishDao.deleteByNoticeId(sysNoticeId);
    }

    /**
     * 获取系统公告类型
     * @return
     */
    public Map<String, NoticeType> getNoticeTypes() {
        Map<String, NoticeType> noticeType = Maps.newHashMap();
        Arrays.asList(SystemNoticeType.values()).forEach(item->noticeType.put(item.getKey(),item.getVal()));
        return noticeType;
    }

    /**
     * 获取系统公告范围
     * @return
     */
    public Map<String, String> getNoticeScopes() {
        Map<String,String> noticeScope = Maps.newHashMap();
        Arrays.asList(SystemNoticeScope.values()).forEach(item->noticeScope.put(item.getKey(),item.getVal()));
        return noticeScope;
    }

    /**
     * 根据用户id获取系统公告
     * @return
     */
    public List<SystemNotice> findSubscriberSystemNotice() {
        String sid = ProviderFactory.getCurrentSubject();
        List<SystemNotice> systemNotices = systemNoticeMapper.findSubscriberSystemNotice(sid);
        return systemNotices;
    }

    /**
     * 根据公告id获取系统公告
     * @param noticeId
     * @return
     */
    public MultiSystemNotice getNoticeById(String noticeId) {
        return systemNoticeMapper.findMultiSystemNoticeById(noticeId);
    }

}

