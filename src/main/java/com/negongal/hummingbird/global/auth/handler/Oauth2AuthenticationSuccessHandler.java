package com.negongal.hummingbird.global.auth.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.negongal.hummingbird.global.auth.token.AuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.negongal.hummingbird.global.auth.application.AuthService;
import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final AuthService authService;
	private final JwtProvider tokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken)authentication;
		Oauth2Attributes attributes = Oauth2Attributes.of(oauth2Token.getPrincipal().getAttributes());
		AuthenticationToken authenticationToken = authService.signIn(attributes);

		String accessToken = tokenProvider.createAccessToken(
			authenticationToken.getPrincipal());
		tokenProvider.createRefreshToken(authenticationToken.getPrincipal(), response);

		String targetUrl;
		if (authenticationToken.getPrincipal().getNickname() == null) {
			targetUrl = "https://hummingbird.kr/firstLogin/success?accessToken=" + accessToken;
		} else {
			targetUrl = "https://hummingbird.kr/login/success?accessToken=" + accessToken;
		}
		getRedirectStrategy().sendRedirect(request, response, targetUrl);

	}

}
