package com.negongal.hummingbird.domain.notification.application;

import com.negongal.hummingbird.domain.notification.dao.NotificationRepository;
import com.negongal.hummingbird.domain.notification.domain.Notification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void performTimeCheckAndPushNotification() {
        log.info("run Notification System");
        LocalDateTime now = LocalDateTime.now();
        List<Notification> findNotification = notificationRepository.findNotificationByNotificationTime(now);

        findNotification.forEach(notification ->
                notificationService.pushTicketingAlertNotification(
                        notification.getPerformance().getId(),
                        notification.getUser().getUserId()));
    }
}