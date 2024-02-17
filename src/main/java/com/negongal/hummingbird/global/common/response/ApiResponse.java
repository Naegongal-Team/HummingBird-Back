package com.negongal.hummingbird.global.common.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiResponse<T> {
	private final ResponseStatus status;
	private final T data;
	private final String code;
	private final String message;

	@Builder
	public ApiResponse(ResponseStatus status, T data, String code, String message) {
		this.status = status;
		this.data = data;
		this.code = code;
		this.message = message;
	}
}