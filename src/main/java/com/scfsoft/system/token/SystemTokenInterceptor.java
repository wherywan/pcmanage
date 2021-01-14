package com.scfsoft.system.token;

import com.alibaba.fastjson.JSON;
import com.scfsoft.sdk.common.api.providers.KeychainProvider;
import com.scfsoft.sdk.common.dto.Response;
import com.scfsoft.sdk.common.exception.ServiceRuntimeException;
import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.sdk.common.utils.StringUtils;
import com.scfsoft.sdk.common.utils.ThreadContextUtils;
import com.scfsoft.sdk.jwt.api.JwtDecoder;
import com.scfsoft.sdk.jwt.api.dto.StandardPayload;
import com.scfsoft.sdk.jwt.config.SdkTokenConfig;
import com.scfsoft.sdk.jwt.dto.DefaultPayload;
import com.scfsoft.system.consts.SystemConsts;
import com.scfsoft.system.enums.SystemError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 令牌拦截器：负责令牌的验证与用户信息的暂存
 * @author James HE
 */
@Slf4j
public class SystemTokenInterceptor implements HandlerInterceptor {

    private final SdkTokenConfig sdkTokenConfig;

    private final JwtDecoder jwtDecoder;

    public SystemTokenInterceptor(SdkTokenConfig sdkTokenConfig, JwtDecoder jwtDecoder) {
        this.sdkTokenConfig = sdkTokenConfig;
        this.jwtDecoder = jwtDecoder;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        log.debug("Token Interceptor: {}", request.getRequestURI());

        try {
            // 1.1 从 header 中取得 auth key
            String authKey = request.getHeader(this.sdkTokenConfig.getHttpHeadAuthKey());

            // 1.2 从参数中获取 token
            if (StringUtils.isEmpty(authKey)) {
                authKey = request.getParameter(this.sdkTokenConfig.getHttpGetTokenKey());
            }

            String token = null;
            StandardPayload tokenPayload;

            if (!StringUtils.isEmpty(authKey)) {
                KeychainProvider keychainProvider = ProviderFactory.getKeychainProvider();
                if (keychainProvider == null) {
                    throw new ServiceRuntimeException(SystemError.XTLSYS010001);
                }
                if (!keychainProvider.validate(authKey)) {
                    throw new ServiceRuntimeException(SystemError.YPWSYS010002);
                }

                // 设置临时 payload
                String subject = keychainProvider.getIdentity(authKey);
                DefaultPayload payload = new DefaultPayload();
                payload.setSubject(subject);
                tokenPayload = payload;
            } else {
                // 2.1 从 header 中取得 token
                token = request.getHeader(this.sdkTokenConfig.getHttpHeadTokenKey());

                // 2.2 从 cookie 中取得 token
                if (StringUtils.isEmpty(token)) {
                    String cookieKey = this.sdkTokenConfig.getCookieTokenKey();
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals(cookieKey)) {
                                token = cookie.getValue();
                                break;
                            }
                        }
                    }
                }

                // 2.3 从参数中获取 token
                if (StringUtils.isEmpty(token)) {
                    token = request.getParameter(this.sdkTokenConfig.getHttpGetTokenKey());
                }

                // 拦截处理
                log.error("token={}", token);
                tokenPayload = this.jwtDecoder.decodeToken(token);
            }

            // 保存为线程变量
            ThreadContextUtils.getResources().clear();
            ThreadContextUtils.put(SystemConsts.THREAD_KEY_TOKEN, token);
            ThreadContextUtils.put(SystemConsts.THREAD_KEY_TOKEN_PAYLOAD, tokenPayload);

        } catch (Exception e) {
            String msg = null;
            if (e instanceof ServiceRuntimeException) {
                msg = e.getMessage();
            } else {
                msg = SystemError.YPWSYS010003.getMessage();
            }

            Response<Void> standardResponse = new Response<>();
            standardResponse.setFlag(-6);
            standardResponse.setMsg(msg);
            // TODO 尝试解码并返回响应中的元数据

            response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print(JSON.toJSONString(standardResponse));

            return false;
        }

        return true;
    }

}
