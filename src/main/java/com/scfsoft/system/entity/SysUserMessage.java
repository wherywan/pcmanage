package com.scfsoft.system.entity;

import com.scfsoft.sdk.das.jpa.po.SimpleBasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * <p>
 * 系统管理 - 用户站内信
 * </p>
 *
 * @author zhuhao
 * @date 2020-03-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYS_USER_MESSAGE")
public class SysUserMessage extends SimpleBasePo<String> {
    private static final long serialVersionUID = 1L;

    /**
     * ID (Label: 主键)
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 系统短消息
     */
    @Column(name = "MESSAGE_ID")
    private String messageId;

    /**
     * 发送人
     */
    @Column(name = "SENDER_ID")
    private String senderId;

    /**
     * 接收人
     */
    @Column(name = "RECEIVER")
    private String receiver;

    /**
     * 消息类型
     */
    @Column(name = "MESSAGE_TYPE")
    private String messageType;

    /**
     * 消息标题
     */
    @Column(name = "TITLE")
    private String title;

    /**
     * 消息内容
     */
    @Column(name = "CONTENT")
    private String content;

    /**
     * 是否已读
     */
    @Column(name = "IS_READ")
    private String isRead;

    /**
     * 回复消息
     */
    @Column(name = "REPLY_ID")
    private String replyId;
}