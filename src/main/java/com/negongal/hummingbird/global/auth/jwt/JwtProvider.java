package com.negongal.hummingbird.global.auth.jwt;

import static com.negongal.hummingbird.global.error.ErrorCode.TOKEN_EXPIRED;
import static com.negongal.hummingbird.global.error.ErrorCode.TOKEN_NOT_MATCHED;
import static com.negongal.hummingbird.global.error.ErrorCode.TOKEN_UNSUPPORTED;

import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider {

    private final String SECRET_KEY;
    private final String COOKIE_REFRESH_TOKEN_KEY;
    private final Long ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60;		// 1hour
    private final Long REFRESH_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 24 * 7 * 2;	// 2week
    private final String AUTHORITIES_KEY = "role";
    private final String PROVIDER = "provider";
    private final String NICKNAME = "nickname";
    private final UserRepository userRepository;

    @Autowired
    public JwtProvider(@Value("${auth.token.secret-key}")String secretKey, @Value("${auth.token.refresh-cookie-key}")String cookieKey, UserRepository userRepository) {
        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.COOKIE_REFRESH_TOKEN_KEY = cookieKey;
        this.userRepository = userRepository;
    }

    public String createAccessToken(Authentication authentication, HttpServletResponse response) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);

        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();

        String userId = user.getName();
        String provider = user.getProvider();
        String nickname = user.getNickname();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                                .setSubject(userId)
                                .claim(AUTHORITIES_KEY, role)
                                .claim(PROVIDER, provider)
                                .claim(NICKNAME, nickname)
                                .setIssuer("naegongal")
                                .setIssuedAt(now)
                                .setExpiration(validity)
                                .compact();
//        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
        return accessToken;
    }

    public void createRefreshToken(Authentication authentication, HttpServletResponse response) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

        String refreshToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setIssuer("naegongal")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();

        saveRefreshToken(authentication, refreshToken);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_KEY, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(REFRESH_TOKEN_EXPIRE_LENGTH/1000)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void saveRefreshToken(Authentication authentication, String refreshToken) {
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        Long userId = user.getUserId();
        String provider = user.getProvider();

        userRepository.updateRefreshToken(userId, provider, refreshToken);
    }

    // Access Token을 검사하고 얻은 정보로 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        CustomUserDetail principal = new CustomUserDetail(
                Long.valueOf(claims.getSubject()),
                String.valueOf(claims.get(PROVIDER)),
                String.valueOf(claims.get(NICKNAME)),
                authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException(TOKEN_EXPIRED.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new JwtException(TOKEN_UNSUPPORTED.getMessage());
        } catch (IllegalArgumentException e) {
            throw new JwtException(TOKEN_NOT_MATCHED.getMessage());
        }
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
