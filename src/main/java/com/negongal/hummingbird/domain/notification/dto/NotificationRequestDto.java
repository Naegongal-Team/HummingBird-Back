package com.negongal.hummingbird.domain.notification.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationRequestDto {

    private Long performanceId;

    private int beforeTime;
}
