package com.negongal.hummingbird.global.auth.model.userInfo;

import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;

import java.util.Map;

public class GoogleUserInfo implements Oauth2UserInfo {
	private Map<String, Object> attributes;

	public GoogleUserInfo(Map<String, Object> attributes) {
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

	@Override
	public Oauth2Attributes extract(String attributeKey, Map<String, Object> attributes) {
		return Oauth2Attributes.builder()
			.attributes(attributes)
			.oauth2Id(getOauthId())
			.provider(getProvider())
			.build();
	}

}
