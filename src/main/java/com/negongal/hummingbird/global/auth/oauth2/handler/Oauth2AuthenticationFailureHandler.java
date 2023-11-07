package com.negongal.hummingbird.global.auth.oauth2.handler;

import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(ResponseUtils.error(ErrorCode.LOGIN_FAILED.getCode(), ErrorCode.LOGIN_FAILED.getMessage()).toString());
        log.info("로그인에 실패했습니다.");
    }
}
