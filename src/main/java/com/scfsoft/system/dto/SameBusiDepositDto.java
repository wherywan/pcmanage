package com.scfsoft.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SameBusiDepositDto {
    private String orgName;

    private String curDate;

    private String orgId;

    private BigDecimal TimeYear;

    private Integer RankYear;

    private String offsetYear;

    private BigDecimal TimeCur;

    private Integer RankCur;

    private String offsetCur;

    private BigDecimal timeUpThanUp;

    private Integer rankUpThanUp;

    private BigDecimal changeCurThanUp;

    private Integer rankCurThanUp;

    private BigDecimal timeYearThanYear;

    private Integer rankYearThanYear;

    private BigDecimal changeCurThanYear;

    private Integer rankCurThanYear;

    @Override
    public String toString() {
        return "SameBusiDepositDto{" +
                "orgName='" + orgName + '\'' +
                ", curDate='" + curDate + '\'' +
                ", orgId='" + orgId + '\'' +
                ", TimeYear=" + TimeYear +
                ", RankYear=" + RankYear +
                ", offsetYear='" + offsetYear + '\'' +
                ", TimeCur=" + TimeCur +
                ", RankCur=" + RankCur +
                ", offsetCur='" + offsetCur + '\'' +
                ", timeUpThanUp=" + timeUpThanUp +
                ", rankUpThanUp=" + rankUpThanUp +
                ", changeCurThanUp=" + changeCurThanUp +
                ", rankCurThanUp=" + rankCurThanUp +
                ", timeYearThanYear=" + timeYearThanYear +
                ", rankYearThanYear=" + rankYearThanYear +
                ", changeCurThanYear=" + changeCurThanYear +
                ", rankCurThanYear=" + rankCurThanYear +
                '}';
    }
}
