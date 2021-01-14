package com.scfsoft.system.controller;

import com.scfsoft.sdk.common.dto.*;
import com.scfsoft.system.entity.jpa.ReportInfo;
import com.scfsoft.system.service.ReportMngService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sfx
 */
@Slf4j
@RestController
@RequestMapping("/report")
public class ReportInfoController {
    @Autowired
    ReportMngService reportMngService;

    @ApiOperation(value = "获取创建报表任务列表", notes = "获取创建报表任务列表 分页")
    @PostMapping("/list")
    public GridResponse<ReportInfo> getReportTask(@RequestBody GridRequest request) {
        Pagination page = request.getData().getPage();
        GridResponse<ReportInfo> response = new GridResponse<>();

        List<ReportInfo> reportInfos = reportMngService.selectReportList(page);
        response.getData().setDatalist(reportInfos);
        response.getData().setPage(page);

        return response;
    }

    @ApiOperation(value = "发布报表填制任务", notes = "发布报表填制任务")
    @PostMapping("/commit")
    public Response publishReport(@RequestBody ReportInfo reportInfo) {
        GridResponse<ReportInfo> result = new GridResponse<>();

        reportMngService.saveReportTask(reportInfo);

        return result;
    }

    @ApiOperation(value = "删除报表任务", notes = "删除报表任务")
    @GetMapping("/delete")
    public Response deleteReport(String id) {
        GridResponse<ReportInfo> result = new GridResponse<>();

        reportMngService.deleteReport(id);

        return result;
    }

}
