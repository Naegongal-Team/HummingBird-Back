package com.negongal.hummingbird.global.auth.oauth2.userInfo;

import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.domain.User;
import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoUserInfo implements Oauth2UserInfo {

    private Map<String, Object> attributes; // getAttributes
//    private Map<String, Object> attributesProperties; // getAttributes
//    private Map<String, Object> attributesAccount; // getAttributes

    public KakaoUserInfo(Map<String,Object> attributes){
        this.attributes = attributes;
//        this.attributesProperties = (Map<String, Object>) attributes.get("properties");
//        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }


    @Override
    public String getOauthId() {
        return attributes.get("id").toString();
    }


    public User toUser() {
        return User.builder()
                .oauth2Id(getOauthId())
                .provider(getProvider())
                .role(Role.USER)
                .build();
    }


}