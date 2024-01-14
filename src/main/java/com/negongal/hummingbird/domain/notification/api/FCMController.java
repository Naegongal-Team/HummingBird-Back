package com.negongal.hummingbird.domain.notification.api;

import com.negongal.hummingbird.domain.notification.application.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FCMController {

    private final FCMService fcmService;

}
