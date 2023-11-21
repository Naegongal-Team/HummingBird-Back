package com.negongal.hummingbird.global.auth.oauth2.handler;

import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // JWT 생성
        String accessToken = tokenProvider.createAccessToken(authentication, response);
        tokenProvider.createRefreshToken(authentication, response);
        log.info("access token={}", accessToken);

        String targetUrl="http://localhost:3000/login/success?accessToken=" + accessToken;
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        if(user.getNickname() == null) {
            //처음 로그인하는 사용자
            targetUrl = "http://localhost:3000/firstLogin?accessToken=" + accessToken;
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        clearAuthenticationAttributes(request);

    }

}
