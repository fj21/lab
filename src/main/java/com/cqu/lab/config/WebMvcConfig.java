package com.cqu.lab.config;

import com.cqu.lab.interceptor.LoginInterceptor;
import com.cqu.lab.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration class for interceptors
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private TokenInterceptor tokenInterceptor;

    /**
     * Add interceptors to the registry
     * @param registry interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register login interceptor
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login", "/api/user/register");

        // Register token interceptor
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login", "/api/user/register");
    }
}
