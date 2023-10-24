package com.negongal.hummingbird.global.error.exception;

import com.negongal.hummingbird.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class HummingbirdException extends RuntimeException {

    private final ErrorCode errorCode;

    public HummingbirdException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage() +  ": " + message);
        this.errorCode = errorCode;
    }

    public HummingbirdException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
