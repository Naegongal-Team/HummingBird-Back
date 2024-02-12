package com.negongal.hummingbird.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetUserResponse {

    @Schema(description = "회원 ID", example = "1")
    private final Long userId;

    @Schema(description = "회원 닉네임", example = "cherry")
    private final String nickname;

    @Schema(description = "회원 프로필 사진", example = "s3링크")
    private final String profileImage;

    @Schema(description = "회원 권한", example = "USER")
    private final String role;

    public static GetUserResponse of(User user){
        return GetUserResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .role(user.getRole().toString())
                .build();
    }
}
