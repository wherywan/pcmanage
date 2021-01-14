package com.scfsoft.system.dao.mybatis;


import com.scfsoft.system.dto.SameBusiSearchParam;
import com.scfsoft.system.entity.jpa.SameBusiPersonalDeposit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SameBusiPersonalDepositMapper {
    public void deleteByCurDate(String curDate, String type);

    public SameBusiPersonalDeposit getYearTotalByParam(SameBusiPersonalDeposit yearTotalParam);

    public SameBusiPersonalDeposit getUpTenDays(String upTenDaysDate,String upOrgId,String upType);
    public SameBusiPersonalDeposit getLastYearDeposit(String lastYearDate,String lastOrgId,String lastType);
    public List<SameBusiPersonalDeposit> getAllDeposit(SameBusiSearchParam searchParam);
}
