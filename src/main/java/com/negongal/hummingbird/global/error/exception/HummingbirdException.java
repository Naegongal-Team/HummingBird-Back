package com.negongal.hummingbird.global.error.exception;

import com.negongal.hummingbird.global.error.ErrorCode;
import lombok.Getter;

@Getter
public abstract class HummingbirdException extends RuntimeException {

    private final ErrorCode code;

    public HummingbirdException(ErrorCode code, String message) {
        super(code.getMessage() +  ": " + message);
        this.code = code;
    }

    public HummingbirdException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
