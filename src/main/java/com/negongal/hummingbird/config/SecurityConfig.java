package com.negongal.hummingbird.config;

import com.negongal.hummingbird.auth.v1.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //    private final Oauth2UserServiceV2 oauth2UserServiceV2;
    private final Oauth2UserService oauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable();

        http
                .authorizeRequests()
                .antMatchers("/login**", "/oauth/**").permitAll()
//                .antMatchers("/user/**").hasRole("USER")
//                .antMatchers("/admin/**","/user/**").hasRole("ADMIN") 권한 설정이 제대로 되지 않는다..
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth/callback/*")
                .and()
                .defaultSuccessUrl("/user/form", true)
                .userInfoEndpoint()
//                .userService(oauth2UserService)
                .userService(oauth2UserService);
        return http.build();

    }
}
