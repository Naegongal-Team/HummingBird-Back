package com.negongal.hummingbird.global.auth.application;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.negongal.hummingbird.global.auth.model.Oauth2Attributes;
import com.negongal.hummingbird.global.auth.model.userInfo.GoogleUserInfo;
import com.negongal.hummingbird.global.auth.model.userInfo.KakaoUserInfo;
import com.negongal.hummingbird.global.auth.model.userInfo.Oauth2UserInfo;
import com.negongal.hummingbird.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oauth2User = super.loadUser(userRequest);

		Oauth2UserInfo oAuth2UserInfo = null;
		Map<String, Object> userAttributes = oauth2User.getAttributes();
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			log.info("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(userAttributes);

		} else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			log.info("카카오 로그인 요청");
			oAuth2UserInfo = new KakaoUserInfo(userAttributes);
		} else {
			throw new OAuth2AuthenticationException(ErrorCode.LOGIN_FAILED.toString());
		}
		Oauth2Attributes attributes = oAuth2UserInfo.extract(userNameAttributeName, userAttributes);
		return new DefaultOAuth2User(List.of(), attributes.convertToMap(), "oauth2Id");
	}
}
