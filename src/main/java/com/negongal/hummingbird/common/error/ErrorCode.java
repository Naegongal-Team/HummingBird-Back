package com.negongal.hummingbird.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /*
    Custom your Error Code.
     */
    ARTIST_GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "ARTIST_GENRE_NOT_FOUND");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
