package com.scfsoft.system.controller;

import com.scfsoft.system.dao.jpa.SysUserEntityDao;
import com.scfsoft.system.dto.UserOrg;
import com.scfsoft.system.entity.jpa.SysRoleResr;
import com.scfsoft.system.entity.jpa.SysUserEntity;
import com.scfsoft.system.service.SysCommonService;
import com.scfsoft.sdk.common.api.dto.StandardPagination;
import com.scfsoft.sdk.common.dto.GridRequest;
import com.scfsoft.sdk.common.dto.GridResponse;
import com.scfsoft.sdk.common.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/userRole")
@Api(value = "用户角色", description = "用户角色", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRoleController {

    @Autowired
    protected SysCommonService sysCommonService;

    @Autowired
    private SysUserEntityDao sysUserEntityDao;

    @ApiOperation(value = "获取用户角色", notes = "获取用户角色")
    @RequestMapping("/getUserRole")
    public String getUserRole(String userId) {

        String userRole = sysCommonService.getUserRole(userId);
        return userRole;
    }

    @ApiOperation(value = "获取角色资源", notes = "获取角色资源")
    @RequestMapping("/getRoleResr")
    public GridResponse<SysRoleResr> getRoleResr(String roleId) {
        List<SysRoleResr> list = sysCommonService.getRoleResr(roleId);
        return GridResponse.newInstance(list);
    }

    @ApiOperation(value = "获取用户业务条线", notes = "获取用户业务条线")
    @RequestMapping("/getUserBl")
    public Response getUserBl(String userId) {
        Response result = Response.newInstance();
        SysUserEntity entity = sysUserEntityDao.findById(userId).orElse(null);
        result.setData(entity);
        return result;
    }

    @ApiOperation(value = "保存用户业务条线", notes = "保存用户业务条线")
    @RequestMapping("/saveBl")
    public Response saveBl(String userId, String businessLine) {
        Response result = Response.newInstance();
        try {
            sysCommonService.saveBl(userId, businessLine);

        } catch (Exception e) {
            result.setFlag(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "查询用户列表", notes = "查询用户列表")
    @RequestMapping("/getBl")
    public GridResponse<SysUserEntity> getBl(@RequestBody GridRequest request) {
        GridResponse<SysUserEntity> result = new GridResponse<>();
        GridResponse.GridData<SysUserEntity> dataList = new GridResponse.GridData<SysUserEntity>();
        try {
            StandardPagination page = request.getData().getPage();
            Map<String, String> reqData = (Map<String, String>)request.getData().getSearchParam();
            String userCode = reqData.get("search_userCode_LIKE");
            List<SysUserEntity> sysUsers = sysCommonService.getBl(userCode, page);
            dataList.setDatalist(sysUsers);
            result.setData(dataList);
        } catch (Exception e) {
            result.setFlag(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取用户机构", notes = "获取用户机构")
    @RequestMapping("/org")
    public Response selectOrgByUser(String userId) {
        Response result = Response.newInstance();
        try {
            UserOrg userOrg = sysCommonService.selectOrgByUser(userId);
            result.setData(userOrg);
        } catch (Exception e) {
            result.setFlag(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取用户机构", notes = "获取用户机构")
    @RequestMapping("/getRoleIdAndOrgId")
    public Response getRoleIdAndOrgId(String userId) {
        Response result = Response.newInstance();
        try {
            UserOrg userOrg = sysCommonService.selectOrgByUser(userId);
            userOrg.setRoleId(sysCommonService.getUserRole(userId));
            result.setData(userOrg);
        } catch (Exception e) {
            result.setFlag(-1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}
