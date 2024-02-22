package com.negongal.hummingbird.global.auth.model.userInfo;

import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.domain.UserStatus;
import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;

import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoUserInfo implements Oauth2UserInfo {

	private final Map<String, Object> attributes; // getAttributes

	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
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
			.status(UserStatus.ACTIVE)
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