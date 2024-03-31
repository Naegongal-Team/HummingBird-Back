package com.negongal.hummingbird.global.auth.jwt;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.model.CustomUserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {

	private final String SECRET_KEY;
	private final String COOKIE_REFRESH_TOKEN_KEY;
	private final Long ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 12;        // 1hour
	private final Long REFRESH_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 24 * 7 * 2;    // 2week
	private final UserService userService;

	@Autowired
	public JwtProvider(@Value("${auth.token.secret-key}") String secretKey,
		@Value("${auth.token.refresh-cookie-key}") String cookieKey, UserService userService) {
		this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
		this.COOKIE_REFRESH_TOKEN_KEY = cookieKey;
		this.userService = userService;
	}

	public String createAccessToken(CustomUserDetail principal) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);

		String role = principal.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		return Jwts.builder()
			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
			.setSubject("auth")
			.claim("oauth2Id", principal.getOauth2Id())
			.claim("role", role)
			.claim("provider", principal.getProvider())
			.claim("nickname", principal.getNickname())
			.claim("status", principal.getStatus())
			.setIssuedAt(now)
			.setExpiration(validity)
			.compact();
	}

	public void createRefreshToken(CustomUserDetail principal, HttpServletResponse response) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

		String refreshToken = Jwts.builder()
			.setSubject("refresh")
			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
			.setIssuedAt(now)
			.setExpiration(validity)
			.compact();
		saveRefreshToken(principal, refreshToken);

		ResponseCookie cookie = createRefreshTokenCookie(refreshToken);
		response.addHeader("Set-Cookie", cookie.toString());
	}

	private void saveRefreshToken(CustomUserDetail principal, String refreshToken) {
		userService.updateRefreshToken(principal.getUserId(), refreshToken);
	}

	public ResponseCookie createRefreshTokenCookie(String refreshToken) {
		return ResponseCookie.from(COOKIE_REFRESH_TOKEN_KEY, refreshToken)
			.domain("hummingbird.kr")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.maxAge(REFRESH_TOKEN_EXPIRE_LENGTH / 1000)
			.path("/")
			.build();
	}

	// Access Token을 검사하고 얻은 정보로 Authentication 객체 생성
	public CustomUserDetail getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		Set<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get("role").toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
		User user = userService.getByOauth2Id(String.valueOf(claims.get("oauth2Id")));

		return CustomUserDetail.of(user, authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.error("토큰이 만료되었습니다.", e);
		} catch (UnsupportedJwtException e) {
			log.error("지원하지 않는 토큰입니다.", e);
		} catch (IllegalArgumentException e) {
			log.error("올바르지 않은 토큰입니다.", e);
		}
		return false;
	}

	// Access Token 만료시 갱신때 사용할 정보를 얻기 위해 Claim 리턴
	public Claims parseClaims(String accessToken) {
		try {
			return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

}
