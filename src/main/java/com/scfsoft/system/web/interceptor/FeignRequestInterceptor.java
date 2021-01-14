package com.scfsoft.system.web.interceptor;

import com.scfsoft.sdk.common.utils.ThreadContextUtils;
import com.scfsoft.sdk.jwt.config.SdkTokenConfig;
import com.scfsoft.system.consts.SystemConsts;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lukejia
 * @date 2020/3/11 10:26
 */
@Slf4j
@RequiredArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {
    private final SdkTokenConfig sdkTokenConfig;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.debug("非 HTTP 请求线程，尝试从线程变量取得令牌");
        String token = null;
        Object tokenObj = ThreadContextUtils.get(SystemConsts.THREAD_KEY_TOKEN);
        if (tokenObj instanceof String) {
            token = (String) tokenObj;
        }
        // 添加token
        requestTemplate.header(this.sdkTokenConfig.getHttpHeadTokenKey(), token);
    }
}
