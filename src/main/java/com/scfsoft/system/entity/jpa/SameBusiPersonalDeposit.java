package com.scfsoft.system.entity.jpa;

import com.scfsoft.sdk.das.jpa.po.Po;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "SAME_BUSI_PERSONAL_DEPOSIT")
public class SameBusiPersonalDeposit extends Po {

    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID",strategy = "uuid")
    @Column(name = "ID")
    private String id;

    @Column(name = "ORG_ID")
    private String orgId;

    @Column(name = "CUR_DATE")
    private String curDate;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "NEW_RANK")
    private Integer newRank;

    @Column(name = "CCB")
    private BigDecimal ccb;

    @Column(name = "ICBC")
    private BigDecimal icbc;

    @Column(name = "ABC")
    private BigDecimal abc;

    @Column(name = "BC")
    private BigDecimal bc;

    @Transient
    private List<String> dayList;
    @Transient
    private BigDecimal ccbTotal;

    @Transient
    private BigDecimal icbcTotal;

    @Transient
    private BigDecimal abcTotal;

    @Transient
    private BigDecimal bcTotal;
}
