package com.scfsoft.system.dao.jpa;

import com.scfsoft.sdk.das.jpa.dao.JpaDao;
import com.scfsoft.system.entity.SysUserMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * <p>
 * 系统管理 - 站内信 Mapper 接口
 * </p>
 *
 * @author zhuhao
 * @date 2020-03-05
 */
public interface SysUserMessageDAO extends JpaDao<SysUserMessage, String> {

    /**
     * 根据用户ID和消息ID删除该条消息
     * @param messageId
     * @param receiver
     * @return
     */
    @Modifying
    @Query("delete from SysUserMessage where receiver=:receiver and messageId=:messageId")
    void deleteByReceiverAndMessageId(@Param("receiver") String receiver, @Param("messageId") String messageId);

    /**
     * 根据用户ID和消息ID查询该条消息
     * @param messageId
     * @param receiver
     * @return
     */
    @Query("select U from SysUserMessage U where U.receiver=:receiver and U.messageId=:messageId")
    SysUserMessage findByReceiverAndMessageId(@Param("receiver") String receiver, @Param("messageId") String messageId);

}
