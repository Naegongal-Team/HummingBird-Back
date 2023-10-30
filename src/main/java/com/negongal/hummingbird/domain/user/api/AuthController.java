package com.negongal.hummingbird.domain.user.api;

import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.auth.jwt.JwtService;
import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import com.negongal.hummingbird.global.auth.utils.CookieUtil;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static com.negongal.hummingbird.global.error.ErrorCode.TOKEN_NOT_FOUND;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;
    CookieUtil cookieUtil;

    @Value("${auth.token.refresh-cookie-key}")
    private String COOKIE_REFRESH_TOKEN_KEY;

    @PostMapping("/refresh")
    public ApiResponse<?> reissueAccessToken(Authentication authentication, @AuthenticationPrincipal CustomUserDetail user, HttpServletResponse response, HttpServletRequest request) {
        validateExistHeader(request);
        Long userId = user.getUserId();
        String refreshToken = cookieUtil.getCookie("refresh", request);

        jwtService.matches(refreshToken, userId);

        String accessToken = jwtProvider.createAccessToken(authentication, response);
        cookieUtil.deleteCookie(COOKIE_REFRESH_TOKEN_KEY, response);
        jwtProvider.createRefreshToken(authentication, response);

        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
        return ResponseUtils.success();
    }

    private void validateExistHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshTokenHeader = cookieUtil.getCookie(COOKIE_REFRESH_TOKEN_KEY, request);
        if (Objects.isNull(authorizationHeader) || Objects.isNull(refreshTokenHeader)) {
            throw new NotExistException(TOKEN_NOT_FOUND);
        }
    }
}
