package com.scfsoft.system.entity;

import com.scfsoft.sdk.das.jpa.po.SimpleBasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


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
@Table(name = "SYS_MESSAGE")
public class SysMessage extends SimpleBasePo<String> {
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
     * 消息类型
     */
    @Column(name = "SEND_TYPE")
    private String sendType;

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
     * 有效期
     */
    @Column(name = "VALID_DATE")
    private Date validDate;


}
