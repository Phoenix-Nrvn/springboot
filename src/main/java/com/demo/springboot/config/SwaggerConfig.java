package com.demo.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * @author LISHANSHAN
 * @ClassName SwaggerConfig
 * @Description TODO
 * @date 2022/05/2022/5/28 18:48
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket restApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("标准接口")
                .apiInfo(apiInfo("Spring Root中使用Swagger2构建RESTful APIS", "1.0"))
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo.springboot.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .description("OK")
                .termsOfServiceUrl("ababa")
                .contact(new Contact("lishanshan", "github.com",  "lss151386@163.com"))
                .version(version)
                .build();
    }
}
