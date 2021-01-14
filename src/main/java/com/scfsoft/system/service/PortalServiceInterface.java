package com.scfsoft.system.service;

import com.scfsoft.system.web.feign.FeignConfig;
import com.scfsoft.sdk.common.dto.GridRequest;
import com.scfsoft.sdk.common.dto.GridResponse;
import com.scfsoft.sdk.common.dto.Request;
import com.scfsoft.sdk.common.dto.Response;
import com.scfsoft.sdk.web.feign.FeignLogConfig;
import com.scfsoft.system.api.dto.Organization;
import com.scfsoft.system.api.dto.Subscriber;
import com.scfsoft.system.api.dto.SubscriberSearchParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author lukejia
 * @date 2020/3/11 10:01
 */
@FeignClient(value = "SERVICE-PORTAL", configuration = { FeignConfig.class, FeignLogConfig.class })
public interface PortalServiceInterface {
    /**
     * 用户列表查询
     * @param request 分页查询参数
     * @return 用户列表
     */
    @ApiOperation("用户列表查询")
    @PostMapping(value = "/system/subscribers", produces = MediaType.APPLICATION_JSON_VALUE)
    GridResponse<Subscriber> getSubscribers(@RequestBody GridRequest<SubscriberSearchParam> request);

    /**
     * 用户查询
     * @param sid      用户ID
     * @param username 用户名称
     * @return 用户信息
     */
    @ApiOperation("用户查询")
    @GetMapping(value = "/system/subscriber", produces = MediaType.APPLICATION_JSON_VALUE)
    Response<Subscriber> getSubscriber(@RequestParam(value = "sid", required = false) String sid, @RequestParam(value = "username", required = false) String username);

    /**
     * 机构查询
     * @return 机构树
     */
    @ApiOperation("机构树查询")
    @GetMapping(value = "/system/org-tree", produces = MediaType.APPLICATION_JSON_VALUE)
    Response<List<Organization>> getOrgTree();

    /**
     * 保存当前用户参数
     * @param request 用户参数
     * @return void
     */
    @ApiOperation("保存当前用户参数")
    @PostMapping(value = "/profile/params/save", produces = MediaType.APPLICATION_JSON_VALUE)
    Response<Void> saveParam(@RequestBody Request<Map<String, Object>> request);
}
