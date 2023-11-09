package com.negongal.hummingbird.global.auth.utils;

import com.negongal.hummingbird.global.auth.oauth2.CustomUserDetail;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;

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