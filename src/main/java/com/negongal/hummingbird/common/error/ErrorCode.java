package com.negongal.hummingbird.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /*
    Custom your Error Code.
     */

    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
