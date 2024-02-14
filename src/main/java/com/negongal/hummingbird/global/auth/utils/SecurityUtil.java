package com.negongal.hummingbird.global.auth.utils;

import com.negongal.hummingbird.global.auth.model.CustomUserDetail;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {
    public static Optional<Long> getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        Long memberId = null;
        if (authentication.getPrincipal() instanceof CustomUserDetail) {
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            memberId = userDetail.getUserId();
        }

        return Optional.ofNullable(memberId);
    }
}