package com.negongal.hummingbird.api.controller;

import com.negongal.hummingbird.api.dto.UserUpdateDto;
import com.negongal.hummingbird.domain.User;
import com.negongal.hummingbird.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@Controller
@RequiredArgsConstructor
public class OauthController {

    private final UserService userService;

    @ResponseBody
    @GetMapping("/user/form")
    public String userInfoForm() {
        log.info("user/form page");
        return "사용자 닉네임 설정 페이지";
    }

    @ResponseBody
    @GetMapping("/user/info")
    public User userinfo(@RequestParam Long id) {
        return userService.findById(id);
    }

    @ResponseBody
    @PatchMapping("/user/info/{uuid}")
    public ResponseEntity<UserUpdateDto> updateNickname(@PathVariable String uuid, @RequestBody UserUpdateDto updateParam) {
        log.info("update nickname");
        Long userId = userService.findByUuid(UUID.fromString(uuid)).getUserId();
        userService.updateUserNickname(userId, updateParam);
        return new ResponseEntity<>(updateParam, HttpStatus.OK);
    }

}