package com.negongal.hummingbird.global.auth.oauth2;

import com.negongal.hummingbird.domain.user.domain.User;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetail implements OAuth2User {

	private final Long userId;
	private final String provider;
	private final String nickname;
	private final String status;
	private final Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> attributes;

	public CustomUserDetail(Long userId, String provider, String nickname, String status,
		Collection<? extends GrantedAuthority> authorities) {
		this.userId = userId;
		this.provider = provider;
		this.nickname = nickname;
		this.status = status;
		this.authorities = authorities;
	}

	public static CustomUserDetail create(User user) {
		List<GrantedAuthority> authorities = Collections.singletonList(user.toGrantedAuthority());

		return new CustomUserDetail(
			user.getId(),
			user.getProvider(),
			user.getNickname(),
			user.getStatus().toString(),
			authorities
		);
	}

	public static CustomUserDetail create(User user, Map<String, Object> attributes) {
		CustomUserDetail userDetails = CustomUserDetail.create(user);
		userDetails.setAttributes(attributes);
		return userDetails;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return String.valueOf(userId);
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
