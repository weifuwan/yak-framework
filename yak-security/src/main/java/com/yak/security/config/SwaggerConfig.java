/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  springfox.documentation.builders.ApiInfoBuilder
 *  springfox.documentation.builders.PathSelectors
 *  springfox.documentation.builders.RequestHandlerSelectors
 *  springfox.documentation.service.ApiInfo
 *  springfox.documentation.service.Contact
 *  springfox.documentation.spi.DocumentationType
 *  springfox.documentation.spring.web.plugins.Docket
 *  springfox.documentation.swagger2.annotations.EnableSwagger2
 */
package com.yak.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration(value="logiSecuritySwaggerConfig")
@EnableSwagger2
public class SwaggerConfig {
    @Bean(value={"logiSecurityCreateRestApi"})
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("logi-security").apiInfo(this.apiInfo()).select().apis(RequestHandlerSelectors.basePackage((String)"com.didiglobal.logi.security.controller")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("logi-security\u5728\u7ebf\u6587\u6863").description("\u7528\u6237\u3001\u9879\u76ee\u3001\u89d2\u8272\u3001\u90e8\u95e8\u3001\u754c\u9762\u6743\u9650\u3001\u8d44\u6e90\u6743\u9650\u3001\u64cd\u4f5c\u65e5\u5fd7\u3001\u6d88\u606f\u901a\u77e5").contact(new Contact("home", "", "")).version("v1.0").build();
    }
}

