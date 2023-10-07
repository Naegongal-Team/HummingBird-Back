package com.negongal.hummingbird.service;

import com.negongal.hummingbird.domain.login.KakaoProfile;
import com.negongal.hummingbird.domain.login.OauthToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class KakaoLogin {
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.token-uri}")
    private String accessTokenUri;
    @Value("${kakao.redirect-uri}")
    private String redirectUrl;
    @Value("${kakao.user-info-uri}")
    private String userInfoUri;
    @Value("${kakao.authorization-grant-type}")
    private String authorization_code;

    public String getToken(String code) {

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", authorization_code);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUrl);
        params.add("code", code);

        //request
        WebClient wc = WebClient.create(accessTokenUri);
        OauthToken oauthToken = wc.post()
                .uri(accessTokenUri)
                .body(BodyInserters.fromFormData(params))
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8") //요청 헤더
                .retrieve()
                .bodyToMono(OauthToken.class)
                .block();


        return oauthToken.getAccess_token();
    }

    public KakaoProfile getUserInfo(String token) {
        log.info("사용자 정보 얻기");

        WebClient wc = WebClient.create(userInfoUri);
        KakaoProfile kakaoProfile = wc.post()
                .uri(userInfoUri)
                .header("Authorization", "Bearer " + token)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(KakaoProfile.class)
                .block();
        log.info("kakaoProfile.nickname={}", kakaoProfile.getProperties().nickname);

        return kakaoProfile;
    }
}
