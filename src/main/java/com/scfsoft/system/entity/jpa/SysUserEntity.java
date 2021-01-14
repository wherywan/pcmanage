package com.scfsoft.system.entity.jpa;

import com.scfsoft.sdk.das.jpa.po.SimpleBasePo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "SYS_USER")
@Getter
@Setter
public class SysUserEntity extends SimpleBasePo<String> {

    @Id
    @Column(
            name = "USER_ID",
            nullable = false
    )
    private String userId;
    @Column(
            name = "EMP_ID"
    )
    private String empId;
    @Column(
            name = "USER_CODE"
    )
    private String userCode;
    @Column(
            name = "MOBILE_PHONE"
    )
    private String mobilePhone;
    @Column(
            name = "WEIX_CODE"
    )
    private String weixCode;
    @Column(
            name = "USER_PASS"
    )
    private String userPass;
    @Column(
            name = "USER_TYPE"
    )
    private String userType;
    @Column(
            name = "USER_HEAD_ID"
    )
    private String userHeadId;
    @Column(
            name = "USER_HEAD_IMG"
    )
    private String userHeadImg;
    @Column(
            name = "USERNAME"
    )
    private String userName;
    @Column(
            name = "USER_STATUS"
    )
    private String userStatus;
    @Column(
            name = "PWD_INIT_FLAG"
    )
    private String pwdInitFlag;
    @Column(
            name = "PWD_ERR_NUM"
    )
    private Long pwdErrNum;
    @Column(
            name = "UNLOCK_TS"
    )
    private Date unlockTs;
    @Column(
            name = "LAST_LOGIN_TS"
    )
    private Date lastLoginTs;
    @Column(
            name = "MAC_CODE"
    )
    private String macCode;
    @Column(
            name = "IP_ADDR"
    )
    private String ipAddr;
    @Column(
            name = "EFFECT_DT"
    )
    private Date effectDt;
    @Column(
            name = "EXPIRE_DT"
    )
    private Date expireDt;
    @Column(
            name = "BUSINESS_LINE"
    )
    private String businessLine;

    @Column(
            name = "ORG_ID"
    )
    private String orgId;


}
