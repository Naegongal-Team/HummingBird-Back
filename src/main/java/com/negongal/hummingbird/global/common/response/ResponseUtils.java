package com.negongal.hummingbird.global.common.response;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {
	/**
	 * 성공
	 */
	public static ApiResponse<Void> success() {
		return success(null);
	}

	public static <T> ApiResponse<Map<String, T>> success(String key, T object) {
		return success(Map.of(key, object));
	}

	public static <T> ApiResponse<T> success(T data) {
		return ApiResponse.<T>builder()
			.data(data)
			.status(ResponseStatus.SUCCESS)
			.build();
	}

	/**
	 * 에러
	 */

	public static ApiResponse<Void> error(String code, String message) {
		return error(code, null, message);
	}

	public static <T> ApiResponse<T> error(String code, T data, String message) {
		return ApiResponse.<T>builder()
			.data(data)
			.status(ResponseStatus.ERROR)
			.code(code)
			.message(message)
			.build();
	}
}