package com.scfsoft.config;

import com.scfsoft.system.service.LocalAttachService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @Author: shenying
 * @Date: 2018/6/6 8:58
 * @Description:
 **/
@Slf4j
@Configuration
public class JcmsConfig {

    @Value("${attach.temp-path}")
    private String tempPath;

    @Value("${attach.root-path}")
    private String rootPath;

    @Bean
    public PathConfig getTempPath() {
        return new PathConfig(tempPath);
    }

    @Bean
    public LocalAttachService getAttachService() {
        LocalAttachService attachService = new LocalAttachService();
        ((LocalAttachService) attachService).setRootDir(rootPath);
        return attachService;
    }

    @Getter
    public static final class PathConfig {
        private String tempPath;

        public PathConfig(String tempPath) {
            this.tempPath = tempPath;
        }
    }

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setMaxInMemorySize(0);
        resolver.setMaxUploadSize(5242880);//上传文件大小 5M 5*1024*1024
        return resolver;
    }

}
