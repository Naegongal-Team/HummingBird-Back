package com.negongal.hummingbird.auth.v1;

import com.negongal.hummingbird.domain.User;

public interface Oauth2UserInfo {
    String getProvider();
    String getEmail();
    public User toUser();
}