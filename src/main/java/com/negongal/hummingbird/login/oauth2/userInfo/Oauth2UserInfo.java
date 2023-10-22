package com.negongal.hummingbird.login.oauth2.userInfo;

import com.negongal.hummingbird.login.domain.User;

public interface Oauth2UserInfo  {
    String getProvider();
    String getOauthId();
    public User toUser();
}