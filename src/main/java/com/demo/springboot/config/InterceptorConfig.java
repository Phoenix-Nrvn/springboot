package com.demo.springboot.config;

import com.demo.springboot.config.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @author LISHANSHAN
 * @ClassName InterceptorConfig
 * @Description TODO
 * @date 2022/06/2022/6/16 15:20
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断token是否合法确定是否执行操作
        // 此处不能通过new来获取一个JwtInterceptor对象，因为样是把对象没有交给Spring中的Bean容器管理的，报错
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register", "/**/export", "/**/import", "/file/**");
    }
}
