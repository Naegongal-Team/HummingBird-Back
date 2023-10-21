package com.negongal.hummingbird.auth.oauth2.userInfo;

import com.negongal.hummingbird.auth.domain.User;

public interface Oauth2UserInfo  {
    String getProvider();
    String getOauthId();
    public User toUser();
}