package com.negongal.hummingbird.domain.user.api;

import com.negongal.hummingbird.domain.user.application.UserService;
import com.negongal.hummingbird.domain.user.dto.UserDetailDto;
import com.negongal.hummingbird.domain.user.dto.UserUpdateDto;
import com.negongal.hummingbird.global.auth.jwt.JwtProviderV2;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;


@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProviderV2 jwtProvider;

    @GetMapping("/user/info")
    public ResponseEntity<UserDetailDto> userDetail(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").substring(7);;
        Claims claims = jwtProvider.parseClaims(accessToken);
        String oauthId = claims.getSubject();
        UserDetailDto user = userService.findByOauthId(oauthId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/user/info")
    public ResponseEntity<UserUpdateDto> userNicknameModify(@RequestBody UserUpdateDto updateParam, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").substring(7);;
        Claims claims = jwtProvider.parseClaims(accessToken);
        String oauthId = claims.getSubject();
        userService.modifyUserNickname(oauthId, updateParam);

        return new ResponseEntity<>(updateParam, HttpStatus.OK);
    }

    @GetMapping("/user/authority")
    public ResponseEntity<Collection<? extends GrantedAuthority>> detailAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }

    @PostMapping("/user/authority")
    public ResponseEntity<Collection<? extends GrantedAuthority>> modifyAuthorityAsAdmin(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").substring(7);;
        Claims claims = jwtProvider.parseClaims(accessToken);
        String oauthId = claims.getSubject();
        Authentication authentication1 = userService.modifyAuthority(oauthId);

        Collection<? extends GrantedAuthority> authorities = authentication1.getAuthorities();

        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }

    @GetMapping("/admin/info")
    public String adminDetail() {
        return "admin page";
    }

}