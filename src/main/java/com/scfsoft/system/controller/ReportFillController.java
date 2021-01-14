package com.scfsoft.system.controller;

import com.alibaba.fastjson.JSON;
import com.scfsoft.sdk.common.dto.GridRequest;
import com.scfsoft.sdk.common.dto.GridResponse;
import com.scfsoft.sdk.common.dto.Pagination;
import com.scfsoft.sdk.common.dto.Response;
import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.system.dto.Attachment;
import com.scfsoft.system.entity.jpa.ReportFill;
import com.scfsoft.system.service.LocalAttachService;
import com.scfsoft.system.service.ReportFillService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @author sfx
 */
@Slf4j
@RestController
@RequestMapping("/fillform")
public class ReportFillController <I extends Attachment>{
    @Autowired
    ReportFillService fillService;

    @ApiOperation(value = "获取填表列表", notes = "获取填表列表 分页")
    @PostMapping("/list")
    public GridResponse<ReportFill> getReportFillList(@RequestBody GridRequest request) {
        Pagination page = request.getData().getPage();
        GridResponse<ReportFill> response = new GridResponse<>();
        List<ReportFill> list = fillService.selectFillFormList(page);
        response.getData().setDatalist(list);
        response.getData().setPage(page);

        return response;
    }

    @RequestMapping(value = "/temp_download")
    @ApiOperation(value = "temp_download", notes = "下载模板")
    public void Download(HttpServletResponse response, HttpServletRequest request, @RequestParam String url) throws Exception {
        String batchId = url;
        String attachId = url;
        fillService.stuffDownload(response, request, batchId, attachId);
    }

    @RequestMapping(value = "/result_download")
    @ApiOperation(value = "result_download", notes = "下载结果文件")
    public void resultDownload(HttpServletResponse response, @RequestParam String reportId) {
        fillService.resultDownload(response, reportId);
    }

    @RequestMapping(value = "/upload-form", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String uploadForm(I attachInfo, @RequestParam("attach") CommonsMultipartFile file) throws Exception {
        Response response = Response.newInstance();

        // 解析文件保存数据库
        DiskFileItem diskFileItem = (DiskFileItem) file.getFileItem();
        File attachFile = diskFileItem.getStoreLocation();
        if (file.getSize() > 0) {
            fillService.saveFillFile(attachInfo, attachFile);
        }

        return JSON.toJSONString(response);
    }

}
