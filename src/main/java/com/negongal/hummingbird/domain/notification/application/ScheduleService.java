package com.negongal.hummingbird.domain.notification.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.negongal.hummingbird.domain.notification.dao.NotificationRepository;
import com.negongal.hummingbird.domain.notification.domain.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final NotificationRepository notificationRepository;
	private final NotificationService notificationService;

	@Scheduled(fixedRate = 60000)
	@Async
	public void performTimeCheckAndPushNotification() {
		log.info("run Notification System");
		LocalDateTime now = LocalDateTime.now().withNano(0).withSecond(0);
		List<Notification> findNotification = notificationRepository.findByNotificationTime(now);

		findNotification.forEach(notification ->
			notificationService.pushTicketingAlertNotification(
				notification.getPerformance().getId(),
				notification.getUser().getId()));
	}
}
