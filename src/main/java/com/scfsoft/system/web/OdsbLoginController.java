package com.scfsoft.system.web;

import com.alibaba.fastjson.JSON;
import com.scfsoft.sdk.common.api.dto.StandardResponse;
import com.scfsoft.sdk.common.dto.Response;
import com.scfsoft.sdk.web.controller.BaseExceptionHandledController;
import com.scfsoft.system.api.dto.Subscriber;
import com.scfsoft.system.api.service.SubscriberService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 模拟从odsb 登录接口
 * @author zhang
 */

@Slf4j
@Controller
@RequestMapping("/odsb-login")
public class OdsbLoginController extends BaseExceptionHandledController {

    @Qualifier("SubscriberServiceDbImpl")
    SubscriberService subscriberService;

    @RequestMapping("/login")
    @ResponseBody
    @ApiOperation(value = "odsb系统登录服务", notes = "odsb登录")
    public StandardResponse login(HttpServletRequest request) {
        Response response = new Response();

        log.info("odsb login  start");
        Cookie[] cookies = request.getCookies();
        Subscriber loginUser= null;
        log.info("cookies:{}", JSON.toJSONString(cookies));
        String userName="";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SM_UID".equals(cookie.getName())) {
                    userName = cookie.getValue();
                    break;
                }
            }
        }
        log.info("userName:{}",userName);
        if(StringUtils.isNotEmpty(userName)){
//            loginUser = subscriberService.login(userName,"",null);
            loginUser = subscriberService.verifySubscriber(userName,"");
            response.setData(loginUser);
        }
        return response;
    }
}
