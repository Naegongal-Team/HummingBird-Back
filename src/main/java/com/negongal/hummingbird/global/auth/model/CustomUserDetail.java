package com.negongal.hummingbird.global.auth.model;

import com.negongal.hummingbird.domain.user.domain.User;

import lombok.Builder;
import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
public class CustomUserDetail implements OAuth2User, Serializable {

	private final Long userId;
	private final String oauth2Id;
	private final String provider;
	private final String nickname;
	private final String status;
	private final Collection<? extends GrantedAuthority> authorities;
	private final Map<String, Object> attributes;

	public static CustomUserDetail of(User user, Set<? extends GrantedAuthority> authorities) {
		return CustomUserDetail.builder()
			.userId(user.getId())
			.oauth2Id(user.getOauth2Id())
			.provider(user.getProvider())
			.nickname(user.getNickname())
			.status(user.getStatus().toString())
			.authorities(authorities)
			.build();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return String.valueOf(this.userId);
	}
}
