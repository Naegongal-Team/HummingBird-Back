package com.negongal.hummingbird.global.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("HummingBird Project API Document")
                .version("1.0.0")
                .contact(new Contact().name("Naegongal-Team").url("https://github.com/Naegongal-Team"))
                .description("HummingBird 프로젝트의 API 명세서입니다.");
    }
}
