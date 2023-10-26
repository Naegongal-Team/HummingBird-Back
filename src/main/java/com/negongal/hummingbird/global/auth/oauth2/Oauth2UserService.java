package com.negongal.hummingbird.global.auth.oauth2;

import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.oauth2.userInfo.GoogleUserInfo;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.global.auth.oauth2.userInfo.KakaoUserInfo;
import com.negongal.hummingbird.global.auth.oauth2.userInfo.Oauth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);

        Oauth2UserInfo oAuth2UserInfo = null;
        Map<String, Object> userAttributes = oauth2User.getAttributes();

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(userAttributes);

        } else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(userAttributes);
        }

        User user = saveOrUpdate(oAuth2UserInfo);

        return CustomUserDetail.create(user, userAttributes);
    }
    private User saveOrUpdate(Oauth2UserInfo oAuth2UserInfo) {
        String oauthId = oAuth2UserInfo.getOauthId();
        String provider = oAuth2UserInfo.getProvider();

        User user = userRepository.findByOauth2IdAndProvider(oauthId, provider)
                    .orElse(oAuth2UserInfo.toUser());

        return userRepository.save(user);
    }
}
