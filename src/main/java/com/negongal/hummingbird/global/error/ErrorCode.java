package com.negongal.hummingbird.global.error;

import com.nimbusds.jwt.JWT;
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

    /**
     * PERFORMANCE
     */
    PERFORMANCE_NOT_EXIST(HttpStatus.BAD_REQUEST, "B001", "PERFORMANCE_NOT_EXIST"),
    PERFORMANCE_HEART_NOT_EXIST(HttpStatus.BAD_REQUEST, "B002", "PERFORMANCE_HEART_NOT_EXIST"),
    PERFORMANCE_HEART_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "B003", "PERFORMANCE_HEART_ALREADY_EXIST"),


    /**
     * USER
     */
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "C001", "USER_NOT_EXIST"),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "C002", "LOGIN_FAILED"),

    /**
     * CHAT ROOM
     */
    CHAT_ROOM_NOT_EXIST(HttpStatus.BAD_REQUEST, "D001", "CHAT_ROOM_NOT_EXIST"),


    /**
     * CHAT MESSAGE
     */

    
    /**
     * Etc
     */
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "F001", "INVALID_TYPE_VALUE"),

    /**
     * JWT Token
     */
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "G001", "TOKEN_EXPIRED"),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "G002","TOKEN_UNSUPPORTED"),
    TOKEN_WRONG(HttpStatus.UNAUTHORIZED, "G003","TOKEN_WRONG"),
    TOKEN_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "G004","TOKEN_NOT_MATCHED"),
    TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "G005","TOKEN_NOT_EXIST");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
