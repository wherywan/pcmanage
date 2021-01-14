package com.scfsoft.system.controller;

import com.scfsoft.sdk.common.dto.GridRequest;
import com.scfsoft.sdk.common.dto.GridResponse;
import com.scfsoft.sdk.common.dto.Pagination;
import com.scfsoft.sdk.common.dto.Request;
import com.scfsoft.system.dto.SearchParam;
import com.scfsoft.system.dto.SysUserDto;
import com.scfsoft.system.dto.UserSearchParam;
import com.scfsoft.system.entity.jpa.PersonalDeposit;
import com.scfsoft.system.service.PersonalDepositService;
import com.scfsoft.system.service.SysCommonService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/personal-deposit")
public class PersonalDepositController {

    @Value("${tempfile.deposit}")
    private String tempFileName;

    @Autowired
    SysCommonService sysCommonService;

    @Autowired
    PersonalDepositService depositService;

    @ApiOperation(value = "查询个人存款报表", notes = "查询个人存款报表 分页")
    @PostMapping("/list")
    public GridResponse<PersonalDeposit> getPersonalDeposit(@RequestBody GridRequest<SearchParam> request) {
        GridResponse<PersonalDeposit> result = new GridResponse<>();
        Pagination page = request.getData().getPage();

        List<PersonalDeposit> list = depositService.selectDepositList(page, request.getData().getSearchParam());

        result.getData().setDatalist(list);
        result.getData().setPage(page);
        return result;
    }

    @RequestMapping(value = "/download")
    @ApiOperation(value = "download", notes = "下载结果文件")
    public void resultDownload(HttpServletResponse response, @RequestParam String statDate, @RequestParam String orgId) {
        depositService.createExcel(response, statDate, tempFileName, orgId);
    }

}
