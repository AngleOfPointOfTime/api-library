package com.anez.config;

import com.anez.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @author cxw
 * @description WebMvcConfiguration
 * @date 2020/11/9 9:16
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    /**
     * 过滤掉不需要进行拦截的url
     */
    private static final String[] EXCLUDE_PATH_PATTERNS = {"/api/token/api_token"};

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/api/**").excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }
}
