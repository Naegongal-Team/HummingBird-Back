package com.negongal.hummingbird.login;

public class UserNotFoundException extends RuntimeException {

    private static final String defaultMessage = "해당하는 유저가 존재하지 않습니다.";

    public UserNotFoundException() {
        super(defaultMessage);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}