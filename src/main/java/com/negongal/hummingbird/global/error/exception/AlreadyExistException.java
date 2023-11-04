package com.negongal.hummingbird.global.error.exception;

import com.negongal.hummingbird.global.error.ErrorCode;

public class AlreadyExistException extends HummingbirdException{
    public AlreadyExistException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
