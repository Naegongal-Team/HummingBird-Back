package com.negongal.hummingbird.global.config;

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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
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
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 핸들링
                .accessDeniedHandler(jwtAccessDeniedHandler); // 인가 실패 핸들링

        http
                .authorizeRequests()
                .antMatchers("/user/**", "/heart/**").hasRole("USER")
                .antMatchers("/admin/**", "/spotify").hasRole("ADMIN")
                .anyRequest().permitAll();

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
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .deleteCookies("refresh")
                .logoutSuccessUrl("/login");

        http
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class);


        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
