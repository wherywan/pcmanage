package com.scfsoft.system.web.feign;

import com.scfsoft.sdk.jwt.config.SdkTokenConfig;
import com.scfsoft.system.web.interceptor.FeignRequestInterceptor;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author lukejia
 * @date 2020/3/11 10:04
 */
@Configuration
public class FeignConfig {

    @Bean
    public FeignRequestInterceptor basicAuthRequestInterceptor(SdkTokenConfig sdkTokenConfig) {
        return new FeignRequestInterceptor(sdkTokenConfig);
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(5, TimeUnit.SECONDS, 10, TimeUnit.SECONDS, false);
    }
}
