package com.scfsoft.system.dao.mybatis;

import com.scfsoft.system.entity.jpa.ReportInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sfx
 * @date 2020/6/12 14:27
 */
@Mapper
public interface ReportMapper {
    List<ReportInfo> fetchReportByPage(@Param("createUser") String createUser);

    void deleteByReportId(@Param("reportId") String reportId);
}
