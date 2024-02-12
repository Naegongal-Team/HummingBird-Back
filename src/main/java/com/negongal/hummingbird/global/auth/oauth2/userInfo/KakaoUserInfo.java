package com.negongal.hummingbird.global.auth.oauth2.userInfo;

import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.domain.UserStatus;

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

}