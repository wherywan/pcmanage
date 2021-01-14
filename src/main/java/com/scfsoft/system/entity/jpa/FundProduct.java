package com.scfsoft.system.entity.jpa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scfsoft.sdk.das.jpa.po.Po;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author sfx
 */
@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "FUND_PRODUCT")
public class FundProduct extends Po {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FUND_CODE")
    @Column(name = "FUND_CODE", nullable = false)
    private String fundCode;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "SELL_BEGIN_DATE")
    private Date sellBeginDate;

    @Column(name = "SELL_END_DATE")
    private Date sellEndDate;

    @Column(name = "BUY_TYPE")
    private String buyType;

    @Column(name = "CREATED_BY")
    private String createUser;

}
