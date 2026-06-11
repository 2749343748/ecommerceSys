package com.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j配置 (OpenAPI 3.0版本)
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("电商管理系统API文档")
                .description("电商管理系统后端接口文档")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Admin")
                        .email("admin@example.com"));
    }
}
