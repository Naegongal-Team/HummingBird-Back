package com.negongal.hummingbird.login.oauth2.handler;

import com.negongal.hummingbird.login.jwt.JwtProviderV2;
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

    private final JwtProviderV2 tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // JWT 생성
        String accessToken = tokenProvider.createAccessToken(authentication, response);
        tokenProvider.createRefreshToken(authentication, response);
        log.info("access token={}", accessToken);

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);

    }

}
