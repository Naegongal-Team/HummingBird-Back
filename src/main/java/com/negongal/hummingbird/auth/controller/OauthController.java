package com.negongal.hummingbird.auth.controller;

import com.negongal.hummingbird.auth.domain.User;
import com.negongal.hummingbird.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
    @GetMapping("/oauth")
    public String oauth() {
        log.info("user/form page");
        return "oauth";
    }

//    @ResponseBody
//    @GetMapping("/user/info")
//    public ResponseEntity<User> userDetail(@RequestBody Long id) {
//        User user = userService.findById(id);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }

    @ResponseBody
    @GetMapping("/user/info")
    public User userDetail(@RequestParam Long id) {
        User user = userService.findById(id);
        return user;
    }

//    @ResponseBody
//    @PatchMapping("/user/info/{uuid}")
//    public ResponseEntity<UserUpdateDto> updateNickname(@PathVariable String uuid, @RequestBody UserUpdateDto updateParam) {
//        log.info("update nickname");
//        Long userId = userService.findByUuid(UUID.fromString(uuid)).getUserId();
//        userService.updateUserNickname(userId, updateParam);
//        return new ResponseEntity<>(updateParam, HttpStatus.OK);
//    }

    @ResponseBody
    @GetMapping("/admin/info")
    public String adminDetail() {
        return "admin page";
    }

    @ResponseBody
    @GetMapping("/user/update")
    public String admin(@RequestParam Long id, Authentication authentication) {
        User user = userService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Authentication newAuth = new OAuth2AuthenticationToken(
                (OAuth2User) authentication.getPrincipal(),
                updatedAuthorities,
                user.getProvider()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "admin page";
    }

}