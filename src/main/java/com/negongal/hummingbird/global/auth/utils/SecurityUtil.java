package com.negongal.hummingbird.global.auth.utils;

import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@NoArgsConstructor
public class SecurityUtil {
    public static Optional<Long> getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        return Optional.of(userDetail.getUserId());
    }
}