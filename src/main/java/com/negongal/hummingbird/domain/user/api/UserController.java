package com.negongal.hummingbird.domain.user.api;

import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.dto.UserDetailDto;
import com.negongal.hummingbird.domain.user.dto.UserDto;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3Uploader uploader;


    @GetMapping("/user/info")
    public ResponseEntity<UserDetailDto> userDetail(Authentication authentication) {
        String oauthId = authentication.getName();
        UserDetailDto user = userService.findByOauthId(oauthId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/user/nickname-check")
    public int nicknameCheck(@RequestParam String nickname) {
        //0이면 중복 1이면 사용 가능
        return userService.findByNickname(nickname);
    }

    @PostMapping( value = "/user/info")
    public ResponseEntity<UserDto> userNicknameAndPhotoAdd(
            @Valid @RequestPart(value = "user") UserDto saveParam,
            @RequestPart(required = false, value = "profileImage") MultipartFile profileImage,
            Authentication authentication) throws IOException {
        String oauthId = authentication.getName();

        String photoUrl = (profileImage == null) ? null : uploader.saveFile(profileImage);
        userService.addUserNicknameAndImage(oauthId, saveParam, photoUrl);

        return new ResponseEntity<>(saveParam, HttpStatus.CREATED);
    }

    @PatchMapping("/user/info")
    public ResponseEntity<UserDto> userNicknameAndPhotoModify(
            @Valid @RequestPart(value = "user") UserDto updateParam,
            @RequestPart(required = false, value = "photo") MultipartFile photo,
            Authentication authentication) throws IOException {
        String oauthId = authentication.getName();

        String photoUrl = (photo == null) ? null : uploader.saveFile(photo);
        userService.modifyUserNicknameAndImage(oauthId, updateParam, photoUrl);

        return new ResponseEntity<>(updateParam, HttpStatus.OK);
    }

    @GetMapping("/user/authority")
    public ResponseEntity<Collection<? extends GrantedAuthority>> detailAuthority(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }

    @GetMapping("/admin/info")
    public String adminDetail() {
        return "admin page";
    }

}