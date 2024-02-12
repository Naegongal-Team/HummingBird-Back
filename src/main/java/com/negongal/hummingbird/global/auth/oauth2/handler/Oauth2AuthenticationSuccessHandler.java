package com.negongal.hummingbird.global.auth.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.negongal.hummingbird.domain.user.dto.response.GetLoginResponse;
import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import com.negongal.hummingbird.global.common.response.ResponseUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtProvider tokenProvider;
	private final ObjectMapper mapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		// JWT 생성
		String accessToken = tokenProvider.createAccessToken(authentication);
		ResponseCookie cookie = createRefreshToken(authentication);

		CustomUserDetail principal = (CustomUserDetail)authentication.getPrincipal();
		GetLoginResponse getLoginResponse = GetLoginResponse.of(principal.getStatus(), principal.getNickname());

		String responseDto = mapper.writeValueAsString(ResponseUtils.success(getLoginResponse));

		response.setStatus(HttpStatus.ACCEPTED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
		response.addHeader("Set-Cookie", cookie.toString());

		PrintWriter writer = response.getWriter();
		writer.write(responseDto);
	}

	private ResponseCookie createRefreshToken(Authentication authentication) {
		String refreshToken = tokenProvider.createRefreshToken(authentication);
		return tokenProvider.createRefreshTokenCookie(refreshToken);
	}

}
