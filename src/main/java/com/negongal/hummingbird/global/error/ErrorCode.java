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
    ARTIST_NOT_EXIST(HttpStatus.BAD_REQUEST, "A002", "ARTIST_NOT_EXIST"),
    ARTIST_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "A003", "ARTIST_ALREADY_EXIST"),
    ARTIST_IMAGE_NOT_EXIST(HttpStatus.NOT_FOUND, "A004", "ARTIST_IMAGE_NOT_EXIST"),
    ALBUM_IMAGE_NOT_EXIST(HttpStatus.NOT_FOUND, "A005", "ALBUM_IMAGE_NOT_EXIST"),

    /**
     * PERFORMANCE
     */
    PERFORMANCE_NOT_EXIST(HttpStatus.BAD_REQUEST, "B001", "PERFORMANCE_NOT_EXIST"),
    PERFORMANCE_HEART_NOT_EXIST(HttpStatus.BAD_REQUEST, "B002", "PERFORMANCE_HEART_NOT_EXIST"),
    PERFORMANCE_HEART_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "B003", "PERFORMANCE_HEART_ALREADY_EXIST"),
    TICKET_NOT_EXIST(HttpStatus.BAD_REQUEST, "B004", "TICKET_NOT_EXIST"),


    /**
     * USER
     */
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "C001", "USER_NOT_EXIST"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "TOKEN_NOT_FOUND"),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "C003", "TOKEN_EXPIRED"),
    TOKEN_NOT_MATCHED(HttpStatus.BAD_REQUEST, "C004", "TOKEN_NOT_MATCHED"),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "C005", "LOGIN_FAILED"),


    /**
     * Etc
     */
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "F001", "INVALID_TYPE_VALUE"),
    PUSH_MESSAGE_FAILED(HttpStatus.BAD_REQUEST, "F002", "PUSH_MESSAGE_FAILED"),
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "F003", "NOTIFICATION_NOT_FOUND"),
    SPOTIFY_CAN_NOT_WORK(HttpStatus.BAD_REQUEST, "F003", "SPOTIFY_CAN_NOT_WORK");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
