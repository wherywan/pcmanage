package com.scfsoft.system.dao.mybatis;

import com.scfsoft.system.entity.jpa.ReportFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sfx
 * @date 2020/6/17 14:27
 */
@Mapper
public interface FillFormMapper {
    List<ReportFill> fetchReportFillByPage(@Param("fillUser") String fillUser);

    List<String> fetchReportDetail(@Param("reportId") String reportId);

    void updateFillResult(ReportFill reportFill);

    void updateDoneNum(@Param("reportId") String reportId);
}
