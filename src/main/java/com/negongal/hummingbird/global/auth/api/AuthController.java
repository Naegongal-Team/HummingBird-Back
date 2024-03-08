package com.negongal.hummingbird.global.auth.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.negongal.hummingbird.domain.user.dto.response.GetLoginResponse;
import com.negongal.hummingbird.global.auth.application.AuthService;
import com.negongal.hummingbird.global.auth.jwt.JwtProvider;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API", description = "")
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final JwtProvider jwtProvider;

	@Operation(summary = "토큰 재발급", description = "쿠키에 저장된 리프레시 토큰으로 토큰을 재발급합니다.")
	@PostMapping("/refresh")
	public ApiResponse<String> reissueToken(HttpServletResponse response, HttpServletRequest request) {
		return ResponseUtils.success(authService.reissueToken(request, response));
	}

	@GetMapping("/login/success")
	public ApiResponse<GetLoginResponse> login(String accessToken) {
		Claims claims = jwtProvider.parseClaims(accessToken);
		GetLoginResponse response = GetLoginResponse.of(claims.get("status").toString(),
			claims.get("nickname").toString(), accessToken);
		return ResponseUtils.success(response);
	}
}
