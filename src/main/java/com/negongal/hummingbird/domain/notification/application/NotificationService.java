package com.negongal.hummingbird.domain.notification.application;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FCMService fcmService;
    private final PerformanceRepository performanceRepository;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final String REGISTER_PERFORM_NOTIFICATION_TITLE = "새로운 공연이 등록되었습니다!";
    private final String REGISTER_PERFORM_NOTIFICATION_BODY = "의 새로운 공연이 등록되었습니다. 지금 바로 확인해보세요!";
    private final String TICKETING_ALERT_NOTIFICATION_TITLE = "티켓팅 시간이 얼마 나지 않았습니다!";
    private final String TICKETING_ALERT_NOTIFICATION_BODY = "의 시간이 얼마 남지 않았습니다! 의자에 앉아서 티켓팅을 기다려 주세요.";

    @Async
    public void pushPerformRegisterNotification(String artistName) {
        /*
        일단 토픽 = 가수 이름으로 생각 중, 프론트앤드 테스트
         */
        log.info("Push Alert -> Register Perform Alert");
        Optional<Artist> findArtist = artistRepository.findByName(artistName);
        if (findArtist.isPresent()) {
            fcmService.sendMessageByTopic(artistName, REGISTER_PERFORM_NOTIFICATION_TITLE,
                    artistName + REGISTER_PERFORM_NOTIFICATION_BODY);
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


}
