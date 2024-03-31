package com.negongal.hummingbird.global.auth.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.negongal.hummingbird.global.auth.application.AuthService;
import com.negongal.hummingbird.global.auth.dto.AuthenticationResult;
import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.auth.model.CustomUserDetail;
import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;
import com.negongal.hummingbird.global.common.response.ResponseUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final AuthService authService;
	private final JwtProvider tokenProvider;
	private final ObjectMapper mapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException{
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken)authentication;
		Oauth2Attributes attributes = Oauth2Attributes.of(oauth2Token.getPrincipal().getAttributes());
		AuthenticationResult authenticationResult = authService.signIn(attributes);

		String accessToken = tokenProvider.createAccessToken(
			authenticationResult.getAuthenticationToken().getPrincipal());
		tokenProvider.createRefreshToken(authenticationResult.getAuthenticationToken().getPrincipal(), response);

		String targetUrl;
		if(authenticationResult.getResponse().getNickname() == null) {
			targetUrl = "http://hummingbird.kr/firstLogin/success?accessToken="+accessToken;
		} else {
			targetUrl = "http://hummingbird.kr/login/success?accessToken="+accessToken;
		}
		getRedirectStrategy().sendRedirect(request, response, targetUrl);

	}

}
