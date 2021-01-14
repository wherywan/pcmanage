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
 * 系统管理 - 系统公告发布表
 * </p>
 *
 * @author guojingyu
 * @date 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYS_NOTICE_PUBLISH")
public class SysNoticePublish extends SimpleBasePo<String> {
    private static final long serialVersionUID = 1L;

    /**
     * ID (Label: 主键)
     */
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    /**
     * 公告ID
     */
    @Column(name = "NOTICE_ID", nullable = false)
    private String noticeId;

    /**
     * 公告范围
     * all-全部可见 org-指定机构可见 role-指定角色可见  user-指定用户id
     */
    @Column(name = "SCOPE", nullable = false)
    private String scope;

    /**
     * 公告范围的具体内容
     * all-all org-可见机构id role-可见角色id user-对应用户id
     */
    @Column(name = "TARGET_ID", nullable = false)
    private String targetId;

}
