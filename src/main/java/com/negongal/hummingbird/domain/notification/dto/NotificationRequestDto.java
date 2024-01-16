package com.negongal.hummingbird.domain.notification.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class NotificationRequestDto {

    private Long performanceId;

    private int beforeTime;
}
