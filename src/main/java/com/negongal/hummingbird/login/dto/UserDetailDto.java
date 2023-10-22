package com.negongal.hummingbird.login.dto;

import com.negongal.hummingbird.login.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailDto {

    private Long userId;
    private String nickname;
    private String profileImage;
    private String provider;

    @Builder
    public UserDetailDto(Long userId, String nickname, String profileImage, String provider) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.provider = provider;
    }

    public static UserDetailDto of(User user){
        return UserDetailDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .provider(user.getProvider())
                .build();
    }
}
