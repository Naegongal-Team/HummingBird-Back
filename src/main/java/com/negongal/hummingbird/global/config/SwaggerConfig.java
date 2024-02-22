package com.negongal.hummingbird.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(apiInfo())
			.components(securityComponent());
	}

	private Info apiInfo() {
		return new Info()
			.title("HummingBird Project API Document")
			.version("1.0.0")
			.contact(new Contact().name("Naegongal-Team").url("https://github.com/Naegongal-Team"))
			.description("HummingBird 프로젝트의 API 명세서입니다.");
	}

	private Components securityComponent() {
		return new Components().addSecuritySchemes("access-token",
			new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.in(SecurityScheme.In.HEADER)
				.name("Authorization")
				.scheme("bearer")
				.bearerFormat("JWT"));
	}
}
