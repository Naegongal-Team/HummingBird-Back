package com.negongal.hummingbird.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetLoginResponse {
	@Schema(description = "회원 상태", example = "ACTIVE")
	private final String status;
	@Schema(description = "회원 닉네임", example = "user1")
	private final String nickname;
	private final String accessToken;

	public GetLoginResponse(String status, String nickname, String accessToken) {
		this.status = status;
		this.nickname = nickname;
		this.accessToken = accessToken;
	}

	public static GetLoginResponse of(String status, String nickname, String accessToken) {
		return new GetLoginResponse(status, nickname, accessToken);
	}
}
