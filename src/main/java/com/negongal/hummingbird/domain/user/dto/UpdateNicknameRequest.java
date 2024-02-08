package com.negongal.hummingbird.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class UpdateNicknameRequest {

    @NotNull(message = "닉네임은 필수 값입니다.")
    @Size(min = 1, max = 8)
    @Schema(description = "회원 닉네임", example = "cherry")
    private String nickname;

}
