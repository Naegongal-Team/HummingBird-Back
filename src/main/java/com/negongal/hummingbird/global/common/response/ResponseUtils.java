package com.negongal.hummingbird.global.common.response;

import java.util.Map;

public class ResponseUtils {
    /**
     * 성공
     */
    public static <T>ApiResponse<T> success(String message) {
        return success(null, message);
    }

    public static <T>ApiResponse<T> success(T data) {
        return success(data, null);
    }

    public static <T>ApiResponse<T> success(String key, T object, String message) {
        return success(Map.of(key, object), message);
    }

    public static <T>ApiResponse success(T data, String message) {
        return ApiResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .data(data)
                .message(message)
                .build();
    }

    /**
     * 에러
     */
    public static <T>ApiResponse error (T data, String message) {
        return ApiResponse.builder()
                .status(ResponseStatus.ERROR)
                .data(data)
                .message(message)
                .build();
    }
}
