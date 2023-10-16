package com.negongal.hummingbird.auth.v2;


import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OauthAttributes {
    GOOGLE("google", (attributes) -> {
        UserProfile UserProfile = new UserProfile();
        UserProfile.setNickname((String) attributes.get("name"));
        UserProfile.setEmail((String) attributes.get("email"));
        return UserProfile;
    }),
    KAKAO("kakao", (attributes) -> {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");


        UserProfile UserProfile = new UserProfile();
        UserProfile.setNickname((String) kakaoProfile.get("nickname"));
        UserProfile.setEmail((String) kakaoAccount.get("email"));
        return UserProfile;
    });

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    OauthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }


    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
