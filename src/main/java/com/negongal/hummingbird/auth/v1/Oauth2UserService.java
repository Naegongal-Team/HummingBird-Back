package com.negongal.hummingbird.auth.v1;

import com.negongal.hummingbird.domain.User;
import com.negongal.hummingbird.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
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
//

        return new PrincipalDetail(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())),
                userAttributes,
                userNameAttributeName
        );
    }
    private User saveOrUpdate(Oauth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();
        String provider = oAuth2UserInfo.getProvider();

        User user = userRepository.findByEmailAndProvider(email, provider)
                .map(m -> m.updateEmail(oAuth2UserInfo.getEmail())) // OAuth 서비스 사이트에서 유저 정보 변경이 있을 수 있기 때문에 우리 DB에도 update
                .orElse(oAuth2UserInfo.toUser());
        log.info("find user email={}",user.getEmail());

        return userRepository.save(user);
    }
}
