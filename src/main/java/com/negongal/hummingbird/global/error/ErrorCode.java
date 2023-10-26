package com.negongal.hummingbird.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * ARTIST
     */
    ARTIST_GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "ARTIST_GENRE_NOT_FOUND"),
    ARTIST_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "A002", "ARTIST_IS_NOT_EXIST"),

    /**
     * PERFORMANCE
     */
    PERFORMANCE_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "B001", "PERFORMANCE_IS_NOT_EXIST"),


    /**
     * USER
     */
    USER_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "C001", "USER_IS_NOT_EXIST"),


    /**
     * Etc
     */
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "F001", "INVALID_TYPE_VALUE");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
