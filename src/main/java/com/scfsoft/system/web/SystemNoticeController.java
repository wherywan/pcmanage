package com.scfsoft.system.web;

import com.scfsoft.system.dto.MultiSystemNotice;
import com.scfsoft.system.dto.NoticeType;
import com.scfsoft.system.dto.SystemNotice;
import com.scfsoft.system.dto.SystemNoticeSearchParam;
import com.scfsoft.sdk.common.dto.*;
import com.scfsoft.system.entity.SysOrg;
import com.scfsoft.system.service.SystemNoticeServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author guojingyu
 */
@AllArgsConstructor
@RestController
public class SystemNoticeController {

    private final SystemNoticeServiceImpl systemNoticeService;

    /**
     * 管理端筛选系统公告
     * @param request
     * @return
     */
    @ApiOperation(value = "筛选系统公告")
    @PostMapping(value = "/system/notices", produces = MediaType.APPLICATION_JSON_VALUE)
    public GridResponse<SystemNotice> getSystemNoticesByPages(@RequestBody GridRequest<SystemNoticeSearchParam> request) {
        Pagination page = request.getData().getPage();

        List<SystemNotice> systemNotices =
                systemNoticeService.getSystemNoticesByPage(request.getData().getSearchParam(), page);

        GridResponse<SystemNotice> response = new GridResponse<>();
        response.getData().setDatalist(systemNotices);
        response.getData().setPage(page);
        return response;
    }

    /**
     * 新增/修改系统公告
     * @param request
     * @return
     */
    @ApiOperation(value = "保存系统公告")
    @PostMapping(value = "/system/notice", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<MultiSystemNotice> saveMultiSystemNotice(@RequestBody  Request<MultiSystemNotice> request) {
        MultiSystemNotice multiSystemNotice = systemNoticeService.saveMultiSystemNotice(request.getData());
        return Response.newInstance(multiSystemNotice);
    }

    /**
     * 删除系统公告
     * @param noticeId
     * @return
     */
    @ApiOperation("删除系统公告")
    @DeleteMapping(value = "/system/notice/{noticeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Void> dropSystemNotice(@PathVariable("noticeId") String noticeId) {
        systemNoticeService.dropSystemNotice(noticeId);
        return Response.newInstance();
    }

    /**
     * 获取系统公告类型
     * @return
     */
    @ApiOperation("获取系统公告类型")
    @GetMapping(value = "/system/notice/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Map<String, NoticeType>> getSystemNoticeTypes() {
        return Response.newInstance(systemNoticeService.getNoticeTypes());
    }

    /**
     * 获取系统公告范围
     * @return
     */
    @ApiOperation("获取系统公告范围")
    @GetMapping(value = "/system/notice/scopes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Map<String, String>> getSystemNoticeScopes() {
        return Response.newInstance(systemNoticeService.getNoticeScopes());
    }

    /**
     * 获取所有的机构
     * @return
     */
    @ApiOperation("获取所有的机构，返回格式为数组")
    @GetMapping(value = "/system/notice/orglist", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<SysOrg>> findAllOrg() {
        return Response.newInstance(systemNoticeService.findAllOrg());
    }
}
