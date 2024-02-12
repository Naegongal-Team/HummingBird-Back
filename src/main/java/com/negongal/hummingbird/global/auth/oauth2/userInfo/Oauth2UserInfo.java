package com.negongal.hummingbird.global.auth.oauth2.userInfo;

import com.negongal.hummingbird.domain.user.domain.User;

public interface Oauth2UserInfo {
	String getProvider();

	String getOauthId();

	User toUser();
}