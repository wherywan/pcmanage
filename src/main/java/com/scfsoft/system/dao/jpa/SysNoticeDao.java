package com.scfsoft.system.dao.jpa;

import com.scfsoft.sdk.das.jpa.dao.JpaDao;
import com.scfsoft.system.entity.SysNotice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 系统管理 - 系统公告表jpa接口
 *
 * @author guojingyu
 * @date 2020-03-05
 */
public interface SysNoticeDao extends JpaDao<SysNotice,String> {

    /**
     * 根据id删除系统公告
     * @param sysNoticeId
     */
    @Modifying
    @Query("delete from SysNotice where id=:sysNoticeId")
    void deleteById(@Param("sysNoticeId") String sysNoticeId);

}
