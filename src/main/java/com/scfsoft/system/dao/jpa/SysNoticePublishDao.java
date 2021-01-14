package com.scfsoft.system.dao.jpa;

import com.scfsoft.sdk.das.jpa.dao.JpaDao;
import com.scfsoft.system.entity.SysNoticePublish;

import java.util.List;

/**
 * @author guojingyu
 */
public interface SysNoticePublishDao extends JpaDao<SysNoticePublish,String> {

    /**
     * 根据公告ID获取公告发布记录id
     * @param noticeId
     * @return
     */
    //SysNoticePublish findByNoticeId(String noticeId);

    /**
     * 根据公告ID删除公告发布记录id
     * @param noticeId
     */
    void deleteByNoticeId(String noticeId);

    /**
     * 根据noticeId获取
     * @param noticeId
     * @return
     */
    List<SysNoticePublish> findByNoticeId(String noticeId);

}
