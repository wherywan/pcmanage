package com.scfsoft.system.controller;

import com.scfsoft.sdk.common.dto.GridRequest;
import com.scfsoft.sdk.common.dto.GridResponse;
import com.scfsoft.sdk.common.dto.Pagination;
import com.scfsoft.system.dto.SameBusiDepositDto;
import com.scfsoft.system.dto.SameBusiSearchParam;
import com.scfsoft.system.dto.SearchParam;
import com.scfsoft.system.entity.jpa.PersonalDeposit;
import com.scfsoft.system.service.SameBuiPersonalDepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/sameBusi-personal-deposit")
public class SameBusiPersonalDepositController {

    @Value("${tempfile.same-busi-deposit}")
    private String tempFileName;

    @Autowired
    private SameBuiPersonalDepositService sameDepositService;

    @PostMapping("/list")
    public GridResponse<SameBusiDepositDto> getSameBusiPersonalDeposits(@RequestBody GridRequest<SameBusiSearchParam> request){
        GridResponse<SameBusiDepositDto> result = new GridResponse<>();
        Pagination page = request.getData().getPage();
        List<SameBusiDepositDto> list = sameDepositService.getSameBusiDepositList(page,request.getData().getSearchParam());
        result.getData().setPage(page);
        result.getData().setDatalist(list);
        return result;
    }

    /**
     * 下载结果
     */
    @GetMapping("/download")
    public void downloadFile(HttpServletResponse response,@RequestParam String curDate){
        sameDepositService.createExcel(response,curDate,tempFileName);
    }
}
