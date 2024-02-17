package com.negongal.hummingbird.global.common.response;

import java.util.Map;

public class ResponseUtils {
	/**
	 * 성공
	 */
	public static <T> ApiResponse success() {
		return ApiResponse.builder()
			.status(ResponseStatus.SUCCESS)
			.build();
	}

	public static <T> ApiResponse<T> success(String key, T object) {
		return success(Map.of(key, object));
	}

	public static <T> ApiResponse success(T data) {
		return ApiResponse.builder()
			.status(ResponseStatus.SUCCESS)
			.data(data)
			.build();
	}

	/**
	 * 에러
	 */

	public static <T> ApiResponse error(String code, String message) {
		return error(code, null, message);
	}

	public static <T> ApiResponse error(String code, T data, String message) {
		return ApiResponse.builder()
			.status(ResponseStatus.ERROR)
			.code(code)
			.data(data)
			.message(message)
			.build();
	}
}
