package com.negongal.hummingbird.domain.user.api;

import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.dto.UserDetailDto;
import com.negongal.hummingbird.domain.user.dto.UserDto;
import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final S3Uploader uploader;
    @Value("${cloud.aws.s3.folder.folderName2}")
    private String userFolder;

    @GetMapping("/login/success")
    public ApiResponse<?> loginSuccess(@RequestParam String accessToken) {
        return ResponseUtils.success(accessToken);
    }

    @GetMapping("/firstLogin")
    public ApiResponse<?> firstLogin(@RequestParam String accessToken) {
        return ResponseUtils.success(accessToken);
    }

    @GetMapping("/user/info")
    public ApiResponse<?> userDetail(@AuthenticationPrincipal CustomUserDetail userDetail) {
        Long userId = userDetail.getUserId();
        UserDetailDto user = userService.findUser(userId);
        return ResponseUtils.success(user);
    }

    @PostMapping("/user/nickname-check")
    public int nicknameCheck(@RequestParam String nickname) {
        //0이면 중복 1이면 사용 가능
        return userService.findByNickname(nickname);
    }

    @PostMapping(value = "/user/info")
    public ApiResponse<?> userNicknameAndPhotoAdd(
            @Valid @RequestPart(value = "user") UserDto saveParam,
            @RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
            @AuthenticationPrincipal CustomUserDetail userDetail) throws IOException {

        Long userId = userDetail.getUserId();
        String photoUrl = (profileImage == null) ? null : uploader.saveFileInFolder(profileImage, userFolder);

        userService.addUserNicknameAndImage(userId, saveParam, photoUrl);

        return ResponseUtils.success();
    }

    @PatchMapping("/user/info")
    public ApiResponse<?> userNicknameAndPhotoModify(
            @Valid @RequestPart(value = "user") UserDto updateParam,
            @RequestPart(required = false, value = "photo") MultipartFile profileImage,
            @AuthenticationPrincipal CustomUserDetail userDetail) throws IOException {

        Long userId = userDetail.getUserId();
        String photoUrl = (profileImage == null) ? null : uploader.saveFileInFolder(profileImage, userFolder);

        userService.modifyUserNicknameAndImage(userId, updateParam, photoUrl);

        return ResponseUtils.success();
    }

    @PostMapping("/remove")
    public ApiResponse<?> remove(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        UserDetailDto user = userService.findUser(customUserDetail.getUserId());
        userService.deleteUser(user);
        return ResponseUtils.success();
    }

}