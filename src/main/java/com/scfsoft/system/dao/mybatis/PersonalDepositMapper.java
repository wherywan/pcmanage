package com.scfsoft.system.dao.mybatis;

import com.scfsoft.system.dto.PersonalDepositDto;
import com.scfsoft.system.dto.SearchParam;
import com.scfsoft.system.entity.jpa.PersonalDeposit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sfx
 * @date 2020/6/17 14:27
 */
@Mapper
public interface PersonalDepositMapper {
    void deleteByStatDate(@Param("statDate") String statDate, @Param("ccy") String ccy);

    List<PersonalDepositDto> selectByStatDates(PersonalDeposit deposit);

    List<PersonalDeposit> fetchDatasByPages(SearchParam searchParam);

    List<PersonalDeposit> fetchSumDatasByPages(SearchParam searchParam);
}
