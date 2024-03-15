package com.negongal.hummingbird.global.auth.application;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.application.UserSignUpService;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.dto.response.GetLoginResponse;
import com.negongal.hummingbird.global.auth.dto.AuthenticationResult;
import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.auth.model.CustomUserDetail;
import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;
import com.negongal.hummingbird.global.auth.token.AuthenticationToken;
import com.negongal.hummingbird.global.auth.utils.CookieUtil;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import com.negongal.hummingbird.global.error.exception.NotMatchException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final UserSignUpService userSignUpService;
	private final JwtProvider jwtProvider;

	@Value("${auth.token.refresh-cookie-key}")
	private String cookieKey;

	@Transactional
	public AuthenticationResult signIn(Oauth2Attributes attributes) {
		User user = userRepository.findByOauth2IdAndProvider(attributes.getOauth2Id(), attributes.getProvider())
			.orElseGet(() -> userSignUpService.signUp(attributes));

		return new AuthenticationResult(AuthenticationToken.of(user, Set.of(user.toGrantedAuthority())),
			GetLoginResponse.of(user.getStatus().toString(), user.getNickname(), null));
	}

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
		CustomUserDetail principal = jwtProvider.getAuthentication(oldAccessToken);
		User user = userService.getByOauth2Id(principal.getOauth2Id());

		//repository 에 저장된 refresh token 과 일치하는지 확인
		String savedToken = user.getRefreshToken();
		validateSameToken(oldRefreshToken, savedToken);

		//access token, refresh token 새로 발급
		String accessToken = jwtProvider.createAccessToken(principal);
		String refreshToken = jwtProvider.createRefreshToken(principal);
		ResponseCookie cookie = jwtProvider.createRefreshTokenCookie(refreshToken);
		response.addHeader("Set-Cookie", cookie.toString());
		response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

		return accessToken;
	}

	public void validateSameToken(String token, String savedRefreshToken) {
		if (!token.equals(savedRefreshToken)) {
			throw new NotMatchException(TOKEN_NOT_MATCHED);
		}
	}
}

