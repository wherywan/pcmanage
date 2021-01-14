package com.scfsoft.system.entity.jpa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scfsoft.sdk.das.jpa.po.Po;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sfx
 */
@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "PERSONAL_DEPOSIT")
public class PersonalDeposit extends Po {
    private static final long serialVersionUID = 1L;

    @Transient
    private String orgName;

    @Column(name = "T_BAL")
    private BigDecimal tBal;

    @Column(name = "T_BAL_THAN_LAST_D")
    private BigDecimal tBalThanLastD;

    @Column(name = "T_BAL_THAN_TENDAYS")
    private BigDecimal tBalThanTendays;

    @Column(name = "T_BAL_THAN_LAST_S")
    private BigDecimal tBalThanLastS;

    @Column(name = "T_BAL_THAN_LAST_M")
    private BigDecimal tBalThanLastM;

    @Column(name = "T_BAL_THAN_BEGIN_Y")
    private BigDecimal tBalThanBeginY;

    @Column(name = "T_BAL_THAN_LAST_Y")
    private BigDecimal tBalThanLastY;

    @Column(name = "A_BAL")
    private BigDecimal aBal;

    @Column(name = "A_BAL_THAN_LAST_D")
    private BigDecimal aBalThanLastD;

    @Column(name = "A_BAL_THAN_TENDAYS")
    private BigDecimal aBalThanTendays;

    @Column(name = "A_BAL_THAN_LAST_S")
    private BigDecimal aBalThanLastS;

    @Column(name = "A_BAL_THAN_LAST_M")
    private BigDecimal aBalThanLastM;

    @Column(name = "A_BAL_THAN_BEGIN_Y")
    private BigDecimal aBalThanBeginY;

    @Column(name = "A_BAL_THAN_LAST_Y")
    private BigDecimal aBalThanLastY;

    @Column(name = "PRICE_SCISSORS")
    private BigDecimal priceScissors;

    @Column(name = "CREATED_BY")
    private String createUser;

    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID", strategy = "uuid")
    @Column(name = "ID ", nullable = false)
    private String id;

    @Column(name = "ORG_ID")
    private String orgId;

    @Column(name = "STAT_DATE")
    private String statDate;

    @Column(name = "CCY")
    private String ccy;

    @Column(name = "UNIT")
    private String unit;

    @Transient
    private List<String> dayList;

    @Transient
    private String pid;
}
