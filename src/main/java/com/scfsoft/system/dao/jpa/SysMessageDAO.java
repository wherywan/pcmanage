package com.scfsoft.system.dao.jpa;

import com.scfsoft.sdk.das.jpa.dao.JpaDao;
import com.scfsoft.system.entity.SysMessage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>
 * 系统管理 - 站内信 Mapper 接口
 * </p>
 *
 * @author zhuhao
 * @date 2020-03-05
 */
public interface SysMessageDAO extends JpaDao<SysMessage, String> {

    /**
     * 查询站内信
     * @return
     */
    @Query("select M from SysMessage M order by M.createTime desc")
    List<SysMessage> findAll();

}
