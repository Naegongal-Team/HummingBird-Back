package com.negongal.hummingbird.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    @NotNull(message = "닉네임은 필수 값입니다.")
    @Length(min=1, max=8)
    private String nickname;

}
