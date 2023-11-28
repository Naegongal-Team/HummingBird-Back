package com.negongal.hummingbird.global.auth;

import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "Auth API", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "토큰 재발급", description = "쿠키에 저장된 리프레시 토큰으로 토큰을 재발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "토큰 재발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "권한 없음"
            )})
    @PostMapping("/refresh")
    public ApiResponse<?> reissueToken(HttpServletResponse response, HttpServletRequest request) {
        String accessToken = authService.reissueToken(request, response);
        return ResponseUtils.success(accessToken);
    }

    @Operation(summary = "엑세스 토큰 반환", description = "엑세스 토큰을 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request")
            })
    @GetMapping("/login/success")
    public ApiResponse<?> loginSuccess(@RequestParam String accessToken) {
        return ResponseUtils.success(accessToken);
    }

    @Operation(summary = "처음 로그인 - 엑세스 토큰 반환", description = "처음 로그인 했을 때 엑세스 토큰을 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "첫번째 로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/firstLogin")
    public ApiResponse<?> firstLogin(@RequestParam String accessToken) {
        return ResponseUtils.success(accessToken);
    }

}
