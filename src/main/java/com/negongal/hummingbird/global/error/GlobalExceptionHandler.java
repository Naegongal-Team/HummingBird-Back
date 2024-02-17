package com.negongal.hummingbird.global.error;

import static com.negongal.hummingbird.global.error.ErrorCode.INVALID_TYPE_VALUE;

import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.global.error.exception.HummingbirdException;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 객체, 파라미터 데이터 값이 유효하지 않은 경우
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ApiResponse<Map<String, Object>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();

		BindingResult bindingResult = e.getBindingResult();
		for (FieldError er : bindingResult.getFieldErrors()) {
			if (errors.containsKey(er.getField())) {
				errors.put(er.getField(), errors.get(er.getField()) + " " + er.getDefaultMessage());
			} else {
				errors.put(er.getField(), er.getDefaultMessage());
			}
		}

		Map<String, Object> data = new HashMap<>();
		data.put("errors", errors);
		return ResponseUtils.error(INVALID_TYPE_VALUE.getCode(), data, INVALID_TYPE_VALUE.getMessage());
	}

	/**
	 * 커스텀 예외
	 */
	@ExceptionHandler(value = HummingbirdException.class)
	public ResponseEntity<ApiResponse<Void>> handleHummingbirdException(HummingbirdException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ResponseUtils.error(errorCode.getCode(), errorCode.getMessage()));
	}
}