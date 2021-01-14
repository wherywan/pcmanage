package com.scfsoft.system.controller;

import com.scfsoft.sdk.common.dto.*;
import com.scfsoft.system.dto.OrgDto;
import com.scfsoft.system.dto.SysUserDto;
import com.scfsoft.system.dto.UserSearchParam;
import com.scfsoft.system.service.SysCommonService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sysCommon")
public class SysCommonController {

    @Autowired
    SysCommonService sysCommonService;

    @ApiOperation(value = "获取该系统所有用户信息", notes = "获取该系统所有用户信息")
    @PostMapping("/AllUsers")
    public GridResponse<SysUserDto> getAllSysUsers(@RequestBody Request<UserSearchParam> request) {
        GridResponse<SysUserDto> result = new GridResponse<>();
        List<SysUserDto> list = sysCommonService.getAllUsers(request.getData());

        result.getData().setDatalist(list);
        return result;
    }

    /**
     * 用户列表查询
     * @param request
     * @return
     */
    @ApiOperation("用户列表查询")
    @PostMapping(value = "/user-list", produces = MediaType.APPLICATION_JSON_VALUE)
    GridResponse<SysUserDto> getSubscribers(@RequestBody GridRequest<UserSearchParam> request) {
        Pagination page = request.getData().getPage();

        List<SysUserDto> subscribers = sysCommonService.fetchSysUsers(request.getData().getSearchParam(), page);

        GridResponse<SysUserDto> response = new GridResponse<>();
        response.getData().setDatalist(subscribers);
        response.getData().setPage(page);
        return response;
    }

    /**
     * 单个用户查询
     * @return
     */
    @ApiOperation("用户信息维护")
    @GetMapping(value = "/one-user", produces = MediaType.APPLICATION_JSON_VALUE)
    Response fetchUser(String id) {
        Response response = Response.newInstance();
        response.setData(sysCommonService.getUserInfo(id));

        return response;
    }

    /**
     * 用户信息维护
     * @param request
     * @return
     */
    @ApiOperation("用户信息维护")
    @PostMapping(value = "/user-save", produces = MediaType.APPLICATION_JSON_VALUE)
    Response saveUser(@RequestBody Request<SysUserDto> request) {
        Response response = Response.newInstance();
        sysCommonService.saveOrUpdUser(request.getData());

        return response;
    }

    /**
     * 机构树查询
     * @param busiScene 业务场景
     * @return
     */
    @ApiOperation("根据ORG_ID查询机构树")
    @GetMapping(value = "/org-tree", produces = MediaType.APPLICATION_JSON_VALUE)
    Response<List<OrgDto>> getOrgTree(String busiScene) {
        Response response = Response.newInstance();

        response.setData(sysCommonService.getOrgTree(busiScene));

        return response;
    }

}
