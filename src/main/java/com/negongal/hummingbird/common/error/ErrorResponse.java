package com.negongal.hummingbird.common.error;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@NoArgsConstructor
public class ErrorResponse {
    private HttpStatus httpStatus;
    private String code;
    private String message;

    @Builder
    public ErrorResponse(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
