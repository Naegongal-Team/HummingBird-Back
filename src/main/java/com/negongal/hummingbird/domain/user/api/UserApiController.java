package com.negongal.hummingbird.domain.user.api;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.negongal.hummingbird.domain.user.application.UserInfoService;
import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.dto.request.UpdateNicknameRequest;
import com.negongal.hummingbird.domain.user.dto.response.GetUserResponse;
import com.negongal.hummingbird.global.auth.model.CustomUserDetail;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "User API", description = "회원 관련 api 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
	private final UserInfoService userInfoService;

	@Operation(summary = "회원 디테일 조회 - 인증 필요", description = "회원 디테일 조회 - 인증 필요",
		security = {@SecurityRequirement(name = "access-token")})
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/user/info")
	public ApiResponse<GetUserResponse> userDetail(@AuthenticationPrincipal CustomUserDetail userDetail) {
		return ResponseUtils.success(userInfoService.getUser(userDetail.getUserId()));
	}

	@Operation(summary = "닉네임 중복 여부 조회", description = "true - 닉네임 중복, false - 닉네임 사용 가능")
	@GetMapping("/nickname-check")
	public ApiResponse<Boolean> nicknameCheck(@RequestParam String nickname) {
		return ResponseUtils.success(userInfoService.nicknameInUse(nickname));
	}

	@Operation(summary = "프로필 이미지 변경 - 인증 필요", description = "프로필 이미지 변경 - 인증 필요",
		security = {@SecurityRequirement(name = "access-token")})
	@PreAuthorize("hasRole('USER')")
	@PutMapping(value = "/user/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<Void> userPhotoAdd(
		@RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
		@AuthenticationPrincipal CustomUserDetail userDetail) {
		userInfoService.updateUserPhoto(userDetail.getUserId(), profileImage);
		return ResponseUtils.success();
	}

	@Operation(summary = "닉네임 변경 - 인증 필요", description = "닉네임을 변경 - 인증 필요",
		security = {@SecurityRequirement(name = "access-token")})
	@PreAuthorize("hasRole('USER')")
	@PatchMapping("/user/nickname")
	public ApiResponse<Void> userNicknameAdd(
		@Valid @RequestBody UpdateNicknameRequest request,
		@AuthenticationPrincipal CustomUserDetail userDetail) {
		userInfoService.updateUserNickname(userDetail.getUserId(), request);
		return ResponseUtils.success();
	}

	@Operation(summary = "fcmToken 등록 - 인증 필요", description = "회원 기기의 토큰을 등록 - 인증 필요",
		security = {@SecurityRequirement(name = "access-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/user/fcm-token")
	public ApiResponse<String> postFCMToken(
		@RequestParam String token,
		@AuthenticationPrincipal CustomUserDetail userDetail) {
		userInfoService.saveFCMToken(token, userDetail.getUserId());
		return ResponseUtils.success(token);
	}

	@Operation(summary = "사용자 탈퇴 - 인증 필요", description = "회원 탈퇴 - 인증 필요",
		security = {@SecurityRequirement(name = "access-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/user/remove")
	public ApiResponse<Void> remove(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
		userService.deleteUser(customUserDetail.getUserId());
		return ResponseUtils.success();
	}

	@Operation(summary = "탈퇴했던 사용자 활성화", description = "탈퇴했었던 회원이 다시 계정을 활성화합니다.",
		security = {@SecurityRequirement(name = "access-token")})
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/user/activate")
	public ApiResponse<Void> activateUser(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
		userService.activateUser(customUserDetail.getUserId());
		return ResponseUtils.success();
	}
}
