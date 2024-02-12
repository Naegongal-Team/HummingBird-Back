package com.negongal.hummingbird.global.config;

import java.util.List;

import com.negongal.hummingbird.global.auth.jwt.JwtAccessDeniedHandler;
import com.negongal.hummingbird.global.auth.jwt.JwtAuthenticationEntryPoint;
import com.negongal.hummingbird.global.auth.jwt.JwtAuthenticationFilter;
import com.negongal.hummingbird.global.auth.jwt.JwtExceptionHandlerFilter;
import com.negongal.hummingbird.global.auth.oauth2.Oauth2UserService;
import com.negongal.hummingbird.global.auth.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.negongal.hummingbird.global.auth.oauth2.handler.Oauth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final Oauth2UserService oauth2UserService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
	private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 핸들링
			.accessDeniedHandler(jwtAccessDeniedHandler); // 인가 실패 핸들링

		http
			.oauth2Login()
			.redirectionEndpoint()
			.baseUri("/oauth/callback/*")
			.and()
			.userInfoEndpoint()
			.userService(oauth2UserService)
			.and()
			.successHandler(oauth2AuthenticationSuccessHandler)
			.failureHandler(oauth2AuthenticationFailureHandler);

		http
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class);

		return http.build();

	}

	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:3000", "http://54.180.120.1:3000", "http://hummingbird.kr"));
		config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "PATCH"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(List.of("Authorization", "refresh"));
		config.setExposedHeaders(List.of("Authorization", "refresh"));
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	static RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
		hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
		return hierarchy;
	}

	@Bean
	static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		return expressionHandler;
	}
}
