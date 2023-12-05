package com.negongal.hummingbird.domain.notification.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    @Scheduled(fixedRate = 600000)
    public void performTimeCheckAndPushNotification() {
        log.info("run Notification System");
    }
}
