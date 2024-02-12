package com.negongal.hummingbird.global.auth;

import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.dto.response.GetLoginResponse;
import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.auth.utils.CookieUtil;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import com.negongal.hummingbird.global.error.exception.NotMatchException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final JwtProvider jwtProvider;

	@Value("${auth.token.refresh-cookie-key}")
	private String cookieKey;

	@Transactional
	public String reissueToken(HttpServletRequest request, HttpServletResponse response) {
		//refresh token 가져와서 검증
		String oldRefreshToken = CookieUtil.getCookie(request, cookieKey)
			.map(Cookie::getValue).orElseThrow(() -> new NotExistException(TOKEN_NOT_EXIST));
		if (!jwtProvider.validateToken(oldRefreshToken)) {
			ResponseUtils.error(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED.getMessage());
			// throw new InvalidException(TOKEN_EXPIRED);
		}

		//만료된 access token 으로 사용자 정보 불러오기
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		String oldAccessToken = token.substring(7);
		Authentication authentication = jwtProvider.getAuthentication(oldAccessToken);
		Long userId = Long.valueOf(authentication.getName());

		//repository 에 저장된 refresh token 과 일치하는지 확인
		User user = userService.getById(userId);
		String savedToken = user.getRefreshToken();
		validateSameToken(oldRefreshToken, savedToken);

		//access token, refresh token 새로 발급
		String accessToken = jwtProvider.createAccessToken(authentication);
		jwtProvider.createRefreshToken(authentication, response);
		response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

		return accessToken;
	}

	public void validateSameToken(String token, String savedRefreshToken) {
		if (!token.equals(savedRefreshToken)) {
			throw new NotMatchException(TOKEN_NOT_MATCHED);
		}
	}

	public GetLoginResponse validateStatus(String accessToken) {
		String userId = jwtProvider.parseClaims(accessToken).getSubject();
		User user = userRepository.findById(Long.valueOf(userId))
			.orElseThrow(() -> new NotExistException(USER_NOT_EXIST));
		String status = user.getStatus().toString();
		return new GetLoginResponse(accessToken, status);
	}
}
