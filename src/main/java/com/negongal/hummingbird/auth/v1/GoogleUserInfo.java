package com.negongal.hummingbird.auth.v1;

import com.negongal.hummingbird.domain.Role;
import com.negongal.hummingbird.domain.User;

import java.util.Map;

public class GoogleUserInfo implements Oauth2UserInfo {
    private Map<String, Object> attributes;
    public GoogleUserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }


    public User toUser() {
        return User.builder()
                .email(getEmail())
                .provider(getProvider())
                .role(Role.USER)
                .build();
    }

}
