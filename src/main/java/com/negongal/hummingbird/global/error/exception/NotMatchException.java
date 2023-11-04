package com.negongal.hummingbird.global.error.exception;

import com.negongal.hummingbird.global.error.ErrorCode;

public class NotMatchException extends HummingbirdException{
    public NotMatchException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotMatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
