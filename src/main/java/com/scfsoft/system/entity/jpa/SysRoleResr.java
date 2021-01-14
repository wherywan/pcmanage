package com.scfsoft.system.entity.jpa;

import com.scfsoft.sdk.das.jpa.po.SimpleBasePo;
import lombok.Getter;

import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "SYS_ROLE_RESR")
public class SysRoleResr extends SimpleBasePo<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID", strategy = "uuid")
    @Column(name = "ROLE_RESR_ID", nullable = false)
    private String roleResrId;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "RESR_ID")
    private String resrId;

    @Column(name = "INCLUDE_FLAG")
    private String includeFlag;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "TENANT_ID")
    private String tenantId;
}
