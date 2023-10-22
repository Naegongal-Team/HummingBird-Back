package com.negongal.hummingbird.global.error;

import com.negongal.hummingbird.global.error.exception.HummingbirdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = HummingbirdException.class)
    public ResponseEntity<ErrorResponse> handleHummingbirdException(HummingbirdException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode().getCode())
                .message(e.getMessage())
                .httpStatus(e.getCode().getHttpStatus())
                .build();

        log.error(errorResponse.toString());

        return ResponseEntity.status(e.getCode().getHttpStatus())
                .body(errorResponse);
    }
}
