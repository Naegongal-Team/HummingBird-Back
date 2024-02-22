package com.negongal.hummingbird.global.auth.utils;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.negongal.hummingbird.global.auth.model.CustomUserDetail;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {
	public static Optional<Long> getCurrentUserId() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			return Optional.empty();
		}

		Long memberId = null;
		if (authentication.getPrincipal() instanceof CustomUserDetail) {
			CustomUserDetail userDetail = (CustomUserDetail)authentication.getPrincipal();
			memberId = userDetail.getUserId();
		}

		return Optional.ofNullable(memberId);
	}
}
