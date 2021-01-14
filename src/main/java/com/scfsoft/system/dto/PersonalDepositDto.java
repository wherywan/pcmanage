package com.scfsoft.system.dto;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonalDepositDto {
    private static final long serialVersionUID = 1L;

    private String orgId;

    private BigDecimal tBal;

    private BigDecimal aBal;

}
