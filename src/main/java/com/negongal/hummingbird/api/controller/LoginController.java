package com.negongal.hummingbird.api.controller;

import com.negongal.hummingbird.domain.User;
import com.negongal.hummingbird.domain.login.KakaoProfile;
import com.negongal.hummingbird.repository.UserRepository;
import com.negongal.hummingbird.service.KakaoLogin;
import com.negongal.hummingbird.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class LoginController {

    private final KakaoLogin kakaoLogin;
    private final UserRepository repository;
    private final LoginService loginService;

    @Autowired
    public LoginController(KakaoLogin kakaoLogin, UserRepository repository, LoginService loginService) {
        this.kakaoLogin = kakaoLogin;
        this.repository = repository;
        this.loginService = loginService;
    }

    @GetMapping("/auth/login")
    public String loginForm() {
        return "login/login";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallBack(String code) {
        String accessToken = kakaoLogin.getToken(code);
        KakaoProfile userInfo = kakaoLogin.getUserInfo(accessToken);

        if(repository.findUserByUserNum(userInfo.getId()).isEmpty()) {
            User user = User.builder()
                            .userNum(userInfo.getId())
                            .nickname(userInfo.getProperties().nickname)
                            .email(userInfo.getKakao_account().getEmail())
                            .build();
            loginService.join(user);
        }
        User kakaoUser = repository.findUserByUserNum(userInfo.getId()).get();

        log.info("user nickName = {}",kakaoUser.getNickname());
        log.info("user email = {}",kakaoUser.getEmail());
        return "redirect:/";
    }

}
