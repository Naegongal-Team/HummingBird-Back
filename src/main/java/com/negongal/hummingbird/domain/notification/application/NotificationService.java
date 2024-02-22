package com.negongal.hummingbird.domain.notification.application;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.notification.dao.NotificationRepository;
import com.negongal.hummingbird.domain.notification.domain.Notification;
import com.negongal.hummingbird.domain.notification.dto.NotificationRequestDto;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.Ticketing;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.ErrorCode;
import com.negongal.hummingbird.global.error.exception.NotExistException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
	private final FCMService fcmService;
	private final PerformanceRepository performanceRepository;
	private final ArtistRepository artistRepository;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;
	private final String REGISTER_PERFORM_NOTIFICATION_TITLE = "새로운 공연이 등록되었습니다!";
	private final String REGISTER_PERFORM_NOTIFICATION_BODY = "의 새로운 공연이 등록되었습니다. 지금 바로 확인해보세요!";
	private final String TICKETING_ALERT_NOTIFICATION_TITLE = "티켓팅 시간이 얼마 나지 않았습니다!";
	private final String TICKETING_ALERT_NOTIFICATION_BODY = "의 시간이 얼마 남지 않았습니다! 의자에 앉아서 티켓팅을 기다려 주세요.";

	public void save(NotificationRequestDto request) {
		Long performanceId = request.getPerformanceId();
		int beforeTime = request.getBeforeTime();
		Performance findPerformance = performanceRepository.findById(performanceId)
			.orElseThrow(() -> new NotExistException(
				ErrorCode.PERFORMANCE_NOT_EXIST
			));
		Long findCurrentUserId = SecurityUtil.getCurrentUserId().orElseThrow(() -> new NotExistException(
			ErrorCode.USER_NOT_EXIST));
		User user = userRepository.findById(findCurrentUserId).orElseThrow(() -> new NotExistException(
			ErrorCode.USER_NOT_EXIST));
		LocalDateTime notificationTime = getNotificationTime(beforeTime, findPerformance);
		if (notificationRepository.findByUserAndPerformance(user, findPerformance).isPresent()) {
			throw new NotExistException(ErrorCode.NOTIFICATION_ALREADY_EXIST);
		}
		log.info("Save Notification");
		Notification notification = Notification.builder()
			.notificationTime(notificationTime)
			.performance(findPerformance)
			.user(user)
			.build();
		notificationRepository.save(notification);
	}

	public void delete(Long notificationId) {
		log.info("Delete Notification");
		Notification findNotification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new NotExistException(
				ErrorCode.NOTIFICATION_NOT_FOUND
			));
		notificationRepository.delete(findNotification);
	}

	@Async
	public void pushPerformRegisterNotification(String artistId) {
        /*
        일단 토픽 = 가수 이름으로 생각 중, 프론트앤드 테스트
         */
		log.info("Push Alert -> Register Perform Alert");
		Optional<Artist> findArtist = artistRepository.findById(artistId);
		if (findArtist.isPresent()) {
			fcmService.sendMessageByTopic(artistId, REGISTER_PERFORM_NOTIFICATION_TITLE,
				artistId + REGISTER_PERFORM_NOTIFICATION_BODY);
		}

	}

	@Async
	public void pushTicketingAlertNotification(Long performId, Long userId) {
		log.info("Push Alert -> TicketTing Alert Perform Alert");
		Optional<Performance> findPerformance = performanceRepository.findById(performId);
		Optional<User> findUser = userRepository.findById(userId);
		if (findPerformance.isPresent() && findUser.isPresent()) {
			String findPerformanceName = findPerformance.get().getName();
			String findUserToken = findUser.get().getFcmToken();

			Optional.ofNullable(findUserToken).ifPresent(token -> fcmService.sendMessageByToken(token,
				TICKETING_ALERT_NOTIFICATION_TITLE,
				findPerformanceName + TICKETING_ALERT_NOTIFICATION_BODY
			));
		}
	}

	private LocalDateTime getNotificationTime(int beforeTime, Performance findPerformance) {
		Comparator<Ticketing> comparatorByStartDate = Comparator.comparing(Ticketing::getStartDate,
			Comparator.nullsLast(Comparator.naturalOrder()));
		Ticketing findPerformanceTicket = findPerformance.getTicketings().stream().min(comparatorByStartDate)
			.orElseThrow(() -> new NotExistException(ErrorCode.TICKET_NOT_EXIST));
		return findPerformanceTicket.getStartDate().minusHours(beforeTime);
	}
}
