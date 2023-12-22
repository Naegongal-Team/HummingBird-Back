package com.negongal.hummingbird.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponseDto {
    private String status;
    private String accessToken;
}
