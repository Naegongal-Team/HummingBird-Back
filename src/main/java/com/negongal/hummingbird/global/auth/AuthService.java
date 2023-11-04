package com.negongal.hummingbird.global.auth;

import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import com.negongal.hummingbird.global.auth.utils.CookieUtil;
import com.negongal.hummingbird.global.error.exception.InvalidException;
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
    private final JwtProvider jwtProvider;

    @Value("${auth.token.refresh-cookie-key}")
    private String cookieKey;

    @Transactional
    public String reissueToken(HttpServletRequest request, HttpServletResponse response) {
        //refresh token 가져와서 검증
        String oldRefreshToken = CookieUtil.getCookie(request, cookieKey)
                .map(Cookie::getValue).orElseThrow(() -> new NotExistException(TOKEN_NOT_FOUND));
        if (!jwtProvider.validateToken(oldRefreshToken)) {
            throw new InvalidException(TOKEN_INVALID);
        }

        //만료된 access token 으로 사용자 정보 불러오기
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String oldAccessToken = token.substring(7);
        Authentication authentication = jwtProvider.getAuthentication(oldAccessToken);
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        Long userId = Long.valueOf(user.getName());

        //repository 에 저장된 refresh token 과 일치하는지 확인
        String savedToken = userRepository.getRefreshTokenById(userId);
        validateSameToken(oldRefreshToken, savedToken);

        //access token, refresh token 새로 발급
        String accessToken = jwtProvider.createAccessToken(authentication, response);
        jwtProvider.createRefreshToken(authentication, response);
        log.info("accessToken={}", accessToken);
        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);

        return accessToken;
    }

    public void validateSameToken(String token, String savedRefreshToken) {
        if (!token.equals(savedRefreshToken)) {
            throw new NotMatchException(TOKEN_NOT_MATCHED);
        }
    }
}
