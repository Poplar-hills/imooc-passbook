package com.imooc.passbook.merchantplatform.config;

import com.imooc.passbook.merchantplatform.security.AuthCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 注册、配置拦截器
 *
 * - 让 AuthCheckInterceptor 只拦截以 "/merchants" 开头的请求
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AuthCheckInterceptor authCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authCheckInterceptor)
                .addPathPatterns("/merchants/**");      // 拦截所有以 "/merchants" 开头的请求
    }
}
