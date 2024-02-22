package com.negongal.hummingbird.global.auth.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseStatus;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		//필요한 권한 없이 접근하려 할때 403 리턴
		setErrorResponse(request, response, accessDeniedException);
	}

	private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> data = new HashMap<>();
		Map<String, Object> error = new HashMap<>();
		error.put("status", HttpServletResponse.SC_FORBIDDEN);
		error.put("message", ex.getMessage());
		error.put("path", request.getServletPath());
		data.put("error", error);

		ApiResponse errorResponse = ApiResponse.builder()
			.status(ResponseStatus.ERROR)
			.data(data)
			.code(null)
			.message("Forbidden")
			.build();

		try {
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
