package com.negongal.hummingbird.domain.user.api;

import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.dto.UserDetailDto;
import com.negongal.hummingbird.domain.user.dto.UserDto;
import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;


@Tag(name = "User API", description = "회원 관련 api 입니다.")
@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final S3Uploader uploader;
    @Value("${cloud.aws.s3.folder.folderName2}")
    private String userFolder;

    @Operation(summary = "회원 디테일 조회", description = "회원 디테일을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "회원 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserDetailDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "권한 없음"
            )})
    @GetMapping("/info")
    public ApiResponse<?> userDetail(@AuthenticationPrincipal CustomUserDetail userDetail) {
        Long userId = userDetail.getUserId();
        UserDetailDto user = userService.findUser(userId);
        return ResponseUtils.success(user);
    }

    @Operation(summary = "회원 닉네임 중복 여부 조회", description = "반환값이 0이면 닉네임 중복, 1이면 닉네임 사용 가능함을 알려줍니다.")
    @PostMapping("/nickname-check")
    public int nicknameCheck(@RequestParam String nickname) {
        //0이면 중복 1이면 사용 가능
        return userService.findByNickname(nickname);
    }

    @Operation(summary = "회원 정보 저장", description = "회원 닉네임과 프로필 사진을 저장합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "회원 정보 저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "권한 없음"
            )})
    @PostMapping(value = "/info", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> userNicknameAndPhotoAdd(
            @Valid @RequestPart(value = "user") UserDto saveParam,
            @RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
            @AuthenticationPrincipal CustomUserDetail userDetail) throws IOException {

        Long userId = userDetail.getUserId();
        String photoUrl = (profileImage == null) ? null : uploader.saveFileInFolder(profileImage, userFolder);

        userService.addUserNicknameAndImage(userId, saveParam, photoUrl);

        return ResponseUtils.success();
    }

    @Operation(summary = "회원 정보 수정", description = "회원 닉네임과 프로필 사진을 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "회원 정보 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "권한 없음"
            )})
    @PatchMapping(value = "/info", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> userNicknameAndPhotoModify(
            @Valid @RequestPart(value = "user") UserDto updateParam,
            @RequestPart(required = false, value = "photo") MultipartFile profileImage,
            @AuthenticationPrincipal CustomUserDetail userDetail) throws IOException {

        Long userId = userDetail.getUserId();
        String photoUrl = (profileImage == null) ? null : uploader.saveFileInFolder(profileImage, userFolder);

        userService.modifyUserNicknameAndImage(userId, updateParam, photoUrl);

        return ResponseUtils.success();
    }

<<<<<<< HEAD
<<<<<<< HEAD
    @PostMapping("/user/fcm-token")
    public ApiResponse postFCMToken(@RequestParam String fcmToken) {
        userService.saveFCMToken(fcmToken);
        return ResponseUtils.success(fcmToken);
=======
=======
    @Operation(summary = "사용자 탈퇴", description = "회원이 탈퇴할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "회원 탈퇴 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "권한 없음"
            )})
>>>>>>> bc5baef (feat. 스웨거 적용)
    @PostMapping("/remove")
    public ApiResponse<?> remove(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        UserDetailDto user = userService.findUser(customUserDetail.getUserId());
        userService.deleteUser(user);
        return ResponseUtils.success();
>>>>>>> e784eb0 (feat. 회원 탈퇴 기능 추가)
    }

}