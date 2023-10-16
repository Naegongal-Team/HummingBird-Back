package com.negongal.hummingbird.auth.v2;

import com.negongal.hummingbird.domain.User;
import com.negongal.hummingbird.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserServiceV2 implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // OAuth 서비스(kakao, google)에서 가져온 유저 정보를 담고있음


        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId(); // OAuth 서비스 이름(ex. kakao, google)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // OAuth 로그인 시 키(pk)가 되는 값
        Map<String, Object> userAttributes = oAuth2User.getAttributes(); // OAuth 서비스의 유저 정보들


        UserProfile userProfile = OauthAttributes.extract(registrationId, userAttributes); // registrationId에 따라 유저 정보를 통해 공통된 UserProfile 객체로 만들어 줌
        userProfile.setProvider(registrationId);
        User user = saveOrUpdate(userProfile);

        Map<String, Object> customAttribute = customAttribute(userAttributes, userNameAttributeName, userProfile, registrationId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())),
                customAttribute,
                userNameAttributeName);

    }

    private Map customAttribute(Map attributes, String userNameAttributeName, UserProfile userProfile, String registrationId) {
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("nickname", userProfile.getNickname());
        customAttribute.put("email", userProfile.getEmail());
        return customAttribute;

    }

    private User saveOrUpdate(UserProfile userProfile) {

        User user = userRepository.findByEmailAndProvider(userProfile.getEmail(), userProfile.getProvider())
                .map(m -> m.updateEmail(userProfile.getEmail())) // OAuth 서비스 사이트에서 유저 정보 변경이 있을 수 있기 때문에 우리 DB에도 update
                .orElse(userProfile.toUser());

        return userRepository.save(user);
    }

}
