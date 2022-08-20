package com.demo.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author LISHANSHAN
 * @ClassName CorsConfig
 * @Description TODO
 * @date 2022/05/2022/5/28 15:13
 */
@Configuration
public class CorsConfig {

    /**
     * Desc: 当前跨域请求的最大有效时长，此处默认是1天
     * @author LISHANSHAN
     * @date 2022/5/28 15:14
     */
    private static final long MAX_AGE = 24*60*60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1.设置访问源地址，任意
        corsConfiguration.addAllowedOrigin("*");
        // 2. 设置访问源请求头
        corsConfiguration.addAllowedHeader("*");
        // 3. 设置访问源请求方法
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(MAX_AGE);
        // 4.对接口配置跨域设置
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

}
