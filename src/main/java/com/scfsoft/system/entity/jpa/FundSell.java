package com.scfsoft.system.entity.jpa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scfsoft.sdk.das.jpa.po.Po;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sfx
 */
@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "FUND_SELL")
public class FundSell extends Po {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID", strategy = "uuid")
    @Column(name = "ID ", nullable = false)
    private String id;

    @Column(name = "ORG_ID")
    private String orgId;

    @Column(name = "FUND_CODE")
    private String fundCode;

    @Column(name = "SELL_DATE")
    private Date sellDate;

    @Column(name = "ACTUAL_AMT")
    private BigDecimal actualAmt;

    @Column(name = "FAIL_AMT")
    private BigDecimal failAmt;

    @Column(name = "SELL_AMT")
    private BigDecimal sellAmt;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CREATED_BY")
    private String createUser;

}
