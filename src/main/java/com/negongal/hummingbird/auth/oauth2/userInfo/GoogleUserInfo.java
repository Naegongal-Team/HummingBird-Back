package com.negongal.hummingbird.auth.oauth2.userInfo;

import com.negongal.hummingbird.auth.domain.Role;
import com.negongal.hummingbird.auth.domain.User;
import com.negongal.hummingbird.auth.oauth2.userInfo.Oauth2UserInfo;

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
    public String getOauthId() {
        return attributes.get("sub").toString();
    }


    public User toUser() {
        return User.builder()
                .oauth2Id(getOauthId())
                .provider(getProvider())
                .role(Role.USER)
                .build();
    }

}
