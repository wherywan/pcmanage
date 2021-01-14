package com.scfsoft.config;

import com.google.common.collect.Lists;
import com.scfsoft.sdk.common.config.SdkInfoConfig;
import com.scfsoft.sdk.web.config.SdkSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author James HE
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(SdkSwaggerConfig sdkSwaggerConfig, SdkInfoConfig infoConfig) {
        // 应用信息
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(infoConfig.getAppDesc())
                .description(infoConfig.getGroupId() + ":" + infoConfig.getArtifactId() + ":" + infoConfig.getVersion())
                .version(infoConfig.getVersion())
                .license(infoConfig.getLicense())
                .build();

        // 公共参数
        List<Parameter> globalParameters = Lists.newArrayList();
        globalParameters.add(new ParameterBuilder()
                .name("Access-Token").description("JWT 访问令牌")
                .scalarExample(sdkSwaggerConfig.getExampleToken())
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false)
                .build()
        );

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(sdkSwaggerConfig.isEnabled())
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(sdkSwaggerConfig.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalParameters)
                .ignoredParameterTypes(HttpServletResponse.class, HttpServletRequest.class);
    }


}
