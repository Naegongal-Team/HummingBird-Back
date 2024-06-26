package com.negongal.hummingbird.domain.user.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateNicknameRequest {

	@NotNull(message = "닉네임은 필수 값입니다.")
	@Size(min = 1, max = 8)
	@Schema(description = "회원 닉네임", example = "cherry")
	private String nickname;

	@JsonCreator
	public UpdateNicknameRequest(@JsonProperty("nickname")String nickname) {
		this.nickname = nickname;
	}
}
