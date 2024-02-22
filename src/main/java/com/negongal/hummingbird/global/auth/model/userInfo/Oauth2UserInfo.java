package com.negongal.hummingbird.global.auth.model.userInfo;

import java.util.Map;

import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;

public interface Oauth2UserInfo {
	String getProvider();

	String getOauthId();

	User toUser();

	Oauth2Attributes extract(String attributeKey, Map<String, Object> attributes);
}