package com.negongal.hummingbird.domain.notification.api;

import com.negongal.hummingbird.domain.notification.application.NotificationService;
import com.negongal.hummingbird.domain.notification.dto.NotificationRequestDto;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse notificationSave(
            @RequestBody @Valid NotificationRequestDto notificationRequestDto) {
        log.info("Execute NotificationSave");
        notificationService.save(notificationRequestDto);
        return ResponseUtils.success();
    }
}
