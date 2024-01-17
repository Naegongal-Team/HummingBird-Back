package com.negongal.hummingbird.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "사용자 DTO")
@Builder
public class UserDto {

    @NotNull(message = "닉네임은 필수 값입니다.")
    @Length(min=1, max=8)
    @Schema(description = "회원 닉네임", example = "cherry")
    private String nickname;

}
