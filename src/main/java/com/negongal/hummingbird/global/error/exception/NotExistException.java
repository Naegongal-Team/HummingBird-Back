package com.negongal.hummingbird.global.error.exception;

import com.negongal.hummingbird.global.error.ErrorCode;

public class NotExistException extends HummingbirdException{
    public NotExistException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
