package com.scfsoft.config;

import com.scfsoft.sdk.jwt.api.JwtDecoder;
import com.scfsoft.sdk.jwt.config.SdkTokenConfig;
import com.scfsoft.system.token.TokenInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC 相关配置
 * @author James HE
 */
@AllArgsConstructor
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final SdkTokenConfig sdkTokenConfig;

    private final JwtDecoder jwtDecoder;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(sdkTokenConfig, jwtDecoder))
                .addPathPatterns("/system/**", "/common/**", "/profile/**","/portal/**","/**");
    }

    @ConditionalOnMissingBean
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

}
