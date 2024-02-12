package com.negongal.hummingbird.domain.user.api;

import com.negongal.hummingbird.domain.user.application.UserInfoService;
import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.dto.UpdateNicknameRequest;
import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@Tag(name = "User API", description = "회원 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final UserInfoService userInfoService;

    @Operation(summary = "회원 디테일 조회", description = "회원 디테일을 조회합니다.")
    @GetMapping("/user/info")
    public ApiResponse<?> userDetail(@AuthenticationPrincipal CustomUserDetail userDetail) {
        return ResponseUtils.success(userInfoService.getUser(userDetail.getUserId()));
    }

    @Operation(summary = "닉네임 중복 여부 조회", description = "true - 닉네임 중복, false - 닉네임 사용 가능")
    @GetMapping("/nickname-check")
    public ApiResponse<Boolean> nicknameCheck(@RequestParam String nickname) {
        return ResponseUtils.success(userInfoService.nicknameInUse(nickname));
    }

    @Operation(summary = "프로필 이미지 변경", description = "프로필 사진을 변경합니다.")
    @PutMapping(value = "/user/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> userPhotoAdd(
            @RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        userInfoService.updateUserPhoto(userDetail.getUserId(), profileImage);
        return ResponseUtils.success();
    }

    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    @PatchMapping("/user/nickname")
    public ApiResponse<?> userNicknameAdd(
        @Valid @RequestBody UpdateNicknameRequest request,
        @AuthenticationPrincipal CustomUserDetail userDetail) {
        userInfoService.updateUserNickname(userDetail.getUserId(), request);
        return ResponseUtils.success();
    }

    @Operation(summary = "fcmToken 등록", description = "회원 기기의 토큰을 등록합니다.")
    @PostMapping("/user/fcm-token")
    public ApiResponse postFCMToken(
        @RequestParam String token,
        @AuthenticationPrincipal CustomUserDetail userDetail) {
        userInfoService.saveFCMToken(token, userDetail.getUserId());
        return ResponseUtils.success(token);
    }

    @Operation(summary = "사용자 탈퇴", description = "회원이 탈퇴할 수 있습니다.")
    @PostMapping("/user/remove")
    public ApiResponse<?> remove(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        userService.deleteUser(customUserDetail.getUserId());
        return ResponseUtils.success();
    }

    @Operation(summary = "탈퇴했던 사용자 활성화", description = "탈퇴했었던 회원이 다시 계정을 활성화합니다.")
    @PostMapping("/user/activate")
    public ApiResponse<?> activateUser(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        userService.activateUser(customUserDetail.getUserId());
        return ResponseUtils.success();
    }
}