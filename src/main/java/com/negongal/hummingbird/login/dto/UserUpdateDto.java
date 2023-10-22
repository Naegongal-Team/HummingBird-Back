package com.negongal.hummingbird.login.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateDto {

    private String nickname;
    private String profileImage;

}
