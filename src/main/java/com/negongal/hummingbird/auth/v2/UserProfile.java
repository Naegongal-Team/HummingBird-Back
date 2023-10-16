package com.negongal.hummingbird.auth.v2;

import com.negongal.hummingbird.domain.Role;
import com.negongal.hummingbird.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {
    private String email;
    private String provider;
    private String nickname;

    public User toUser() {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .provider(provider)
                .role(Role.USER)
                .build();
    }
}
