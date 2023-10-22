package com.negongal.hummingbird.login;

public class AccessDeniedException extends RuntimeException{

    private static final String defaultMessage = "권한이 없습니다.";

    public AccessDeniedException() {
        super(defaultMessage);
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
