package com.negongal.hummingbird.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMDto {
    private Long userId;
    private String title;
    private String body;

    @Builder
    public FCMDto(Long userId, String title, String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}
