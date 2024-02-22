package com.negongal.hummingbird.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Role {

	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private final String authority;

	Role(String authority) {
		this.authority = authority;
	}
}