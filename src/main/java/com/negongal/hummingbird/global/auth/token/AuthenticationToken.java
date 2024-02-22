package com.negongal.hummingbird.global.auth.token;

import java.util.Set;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.model.CustomUserDetail;

public class AuthenticationToken extends AbstractAuthenticationToken {

	private final CustomUserDetail userDetail;

	public AuthenticationToken(CustomUserDetail userDetail, Set<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.userDetail = userDetail;
	}

	public static AuthenticationToken of(User user, Set<? extends GrantedAuthority> authorities) {
		return new AuthenticationToken(CustomUserDetail.of(user, authorities), authorities);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public CustomUserDetail getPrincipal() {
		return this.userDetail;
	}
}
