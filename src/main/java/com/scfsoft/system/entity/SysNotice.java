package com.scfsoft.system.entity;

import com.scfsoft.sdk.das.jpa.po.SimpleBasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <p>
 * 系统管理 - 系统公告表
 * </p>
 *
 * @author guojingyu
 * @date 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYS_NOTICE")
public class SysNotice extends SimpleBasePo<String> {
    private static final long serialVersionUID = 1L;

    /**
     * ID (Label: 主键)
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 公告类型
     */
    @Column(name = "NOTICE_TYPE")
    private String noticeType;

    /**
     * 标题
     */
    @Column(name = "TITLE")
    private String title;

    /**
     * 公告内容（富文本HTML字符串）
     */
    @Column(name = "CONTENT")
    private String content;

    /**
     * 发布时间
     */
    @Column(name = "PUBLISH_TIME")
    private Date publishTime;

    /**
     * 是否可见：Y-可见 N-不可见
     */
    @Column(name = "IS_VISIBLE")
    private String isVisible;

}
