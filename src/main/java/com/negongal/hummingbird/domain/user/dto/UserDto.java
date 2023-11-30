package com.negongal.hummingbird.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "사용자 DTO")
public class UserDto {

    @NotNull(message = "닉네임은 필수 값입니다.")
    @Length(min=1, max=8)
    @Schema(description = "회원 닉네임", nullable = false, example = "cherry")
    private String nickname;

}
