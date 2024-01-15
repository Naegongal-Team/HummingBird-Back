package com.negongal.hummingbird.domain.user.dto;

import com.negongal.hummingbird.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원 디테일 DTO")
public class UserDetailDto {

    @Schema(description = "회원 ID", example = "1")
    private Long userId;

    @Schema(description = "회원 닉네임", example = "cherry")
    private String nickname;

    @Schema(description = "회원 프로필 사진", example = "s3링크")
    private String profileImage;

    @Schema(description = "회원 권한", example = "USER")
    private String role;

    @Builder
    public UserDetailDto(Long userId, String nickname, String profileImage, String role) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
    }

    public static UserDetailDto of(User user){
        return UserDetailDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .role(user.getRole().toString())
                .build();
    }
}
