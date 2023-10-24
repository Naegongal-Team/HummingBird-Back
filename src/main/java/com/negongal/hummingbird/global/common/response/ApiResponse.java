package com.negongal.hummingbird.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApiResponse<T> {
    private ResponseStatus status;
    private T data;
    private String code;
    private String message;

    @Builder
    public ApiResponse(ResponseStatus status, T data, String code, String message) {
        this.status = status;
        this.data = data;
        this.code = code;
        this.message = message;
    }
}
