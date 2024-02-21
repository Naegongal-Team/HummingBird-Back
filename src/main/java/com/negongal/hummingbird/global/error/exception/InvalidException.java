package com.negongal.hummingbird.global.error.exception;

import com.negongal.hummingbird.global.error.ErrorCode;

public class InvalidException extends HummingbirdException {
	public InvalidException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public InvalidException(ErrorCode errorCode) {
		super(errorCode);
	}
}
