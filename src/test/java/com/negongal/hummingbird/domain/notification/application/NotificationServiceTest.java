package com.negongal.hummingbird.domain.notification.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import com.negongal.hummingbird.HummingbirdApplication;
import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.notification.dao.NotificationRepository;
import com.negongal.hummingbird.domain.notification.dto.NotificationRequestDto;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.dao.TicketingRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.profiles.active=test")
class NotificationServiceTest {
    @InjectMocks
    @Autowired
    private NotificationService notificationService;
    @MockBean
    private FCMService fcmService;
    @Mock
    private Performance mockPerformance;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeAll
    static void beforeALl() {
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    static void afterAll() {
        mockedSecurityUtil.close();
    }

    @BeforeEach
    public void setUp() {
        Artist artist = Artist.builder()
                .id("159123")
                .name("익명")
                .build();
        artistRepository.save(artist);
        Performance performance = Performance.builder()
                .name("테스트 공연이름")
                .location("내 집")
                .runtime(180L)
                .artist(artist)
                .build();
        performanceRepository.save(performance);
        User user = User.builder()
                .oauth2Id("id")
                .nickname("헤헤")
                .provider("제공")
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    @DisplayName("없는 공연에 알림 저장")
    @Transactional
    @Test
    void saveNotExistPerformanceTest() {
        // Given
        Long performanceId = 9999L;
        Long userId = 1L;
        int beforeTime = 1;
        NotificationRequestDto request = NotificationRequestDto.builder()
                .performanceId(performanceId)
                .beforeTime(1)
                .build();
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));

        Throwable notExistException = assertThrows(NotExistException.class, () ->
                notificationService.save(request));
        assertEquals("PERFORMANCE_NOT_EXIST", notExistException.getMessage());
    }

    @DisplayName("로그인하지 않은 채로 알림 저장")
    @Transactional
    @Test
    void saveNotificationNotExistUserTest() {
        // Given
        Long performanceId = 1L;
        int beforeTime = 1;
        NotificationRequestDto request = NotificationRequestDto.builder()
                .performanceId(performanceId)
                .beforeTime(1)
                .build();
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());

        Throwable notExistException = assertThrows(NotExistException.class, () ->
                notificationService.save(request));
        assertEquals("USER_NOT_EXIST", notExistException.getMessage());
    }

    @DisplayName("알림 저장")
    @Transactional
    @Test
    void saveNotificationNotTicketTest() {
        // Given
        Long performanceId = 1L;
        Long userId = 1L;
        int beforeTime = 1;
        NotificationRequestDto request = NotificationRequestDto.builder()
                .performanceId(performanceId)
                .beforeTime(1)
                .build();
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));

        Throwable notExistException = assertThrows(NotExistException.class, () ->
                notificationService.save(request));
        assertEquals("TICKET_NOT_EXIST", notExistException.getMessage());
    }

    @DisplayName("공연에서 예매처가 존재하지 않은 채로 알림 저장")
    @Transactional
    @Test
    void saveNotificationTest() {
        // Given
        Long performanceId = 1L;
        Long userId = 1L;
        int beforeTime = 1;
        NotificationRequestDto request = NotificationRequestDto.builder()
                .performanceId(performanceId)
                .beforeTime(1)
                .build();
        BDDMockito.given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(1L));
    }
}