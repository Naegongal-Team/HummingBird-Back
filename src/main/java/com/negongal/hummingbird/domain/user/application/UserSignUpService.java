package com.negongal.hummingbird.domain.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserSignUpService {

	private final UserService userService;

	@Transactional
	public User signUp(Oauth2Attributes attributes) {
		return userService.saveUser(attributes);
	}
}
