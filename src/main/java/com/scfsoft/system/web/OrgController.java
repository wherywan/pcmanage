package com.scfsoft.system.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.scfsoft.sdk.common.dto.Request;
import com.scfsoft.sdk.common.dto.Response;
import com.scfsoft.sdk.common.utils.SpringUtils;
import com.scfsoft.system.api.dto.Organization;
import com.scfsoft.system.api.dto.OrganizationStatistic;
import com.scfsoft.system.enums.OrgType;
import com.scfsoft.system.service.OrgDepService;
import com.scfsoft.system.service.OrgService;
import com.scfsoft.system.service.SyncOrgService;
import com.scfsoft.system.web.SystemOrgController;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author James HE
 */
@RestController
public class OrgController extends SystemOrgController {

    @Autowired
    private OrgDepService orgDepService;

    public OrgController(OrgService orgService) {
        super(orgService);
    }

    @Override
    public Response<Void> getOrgSync() {
        SyncOrgService syncOrgService = SpringUtils.getBean(SyncOrgService.class);
        syncOrgService.syncOrg();
        return Response.newInstance();
    }

    @Override
    public Response<List<Organization>> saveOrgTree(Request<List<Organization>> request) {
        request.getData().forEach(org -> org.setId(OrgType.O04.getKey().equals(org.getOrgType()) ? org.getId() : org.getPid()));
        return super.saveOrgTree(request);
    }

    /**
     * 获取各个支行的部门信息
     * @return
     */
    @ApiOperation("机构信息统计")
    @GetMapping(value = "/system/org/dep", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Map<String, String>> getAllDep() {
        Response response = Response.newInstance();
        response.setData(orgDepService.getAllDep());

        return response;
    }
}
