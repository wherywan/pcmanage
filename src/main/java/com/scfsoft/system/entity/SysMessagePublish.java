package com.scfsoft.system.entity;

import com.scfsoft.sdk.das.jpa.po.SimpleBasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * <p>
 * 系统管理 - 站内信
 * </p>
 *
 * @author zhuhao
 * @date 2020-03-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYS_MESSAGE_PUBLISH")
public class SysMessagePublish extends SimpleBasePo<String> {
    private static final long serialVersionUID = 1L;

    /**
     * ID (Label: 主键)
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 消息ID
     */
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "MESSAGE_ID")
    private String messageId;

    /**
     * 消息类型
     */
    @Column(name = "SEND_TYPE")
    private String sendType;

    /**
     * 消息目标
     */
    @Column(name = "TARGET_ID")
    private String targetId;


}
