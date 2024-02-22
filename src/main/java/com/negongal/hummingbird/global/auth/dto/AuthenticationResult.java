package com.negongal.hummingbird.global.auth.dto;

import com.negongal.hummingbird.domain.user.dto.response.GetLoginResponse;
import com.negongal.hummingbird.global.auth.token.AuthenticationToken;

import lombok.Getter;

@Getter
public class AuthenticationResult {

	private final AuthenticationToken authenticationToken;
	private final GetLoginResponse response;

	public AuthenticationResult(AuthenticationToken authenticationToken, GetLoginResponse response) {
		this.authenticationToken = authenticationToken;
		this.response = response;
	}
}
