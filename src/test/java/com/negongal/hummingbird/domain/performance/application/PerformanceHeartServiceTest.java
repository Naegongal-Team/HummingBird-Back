package com.negongal.hummingbird.domain.performance.application;

import static com.negongal.hummingbird.global.error.ErrorCode.PERFORMANCE_HEART_ALREADY_EXIST;
import static com.negongal.hummingbird.global.error.ErrorCode.PERFORMANCE_HEART_NOT_EXIST;
import static com.negongal.hummingbird.global.error.ErrorCode.PERFORMANCE_NOT_EXIST;
import static com.negongal.hummingbird.global.error.ErrorCode.USER_NOT_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import com.negongal.hummingbird.domain.performance.PerformanceTestHelper;
import com.negongal.hummingbird.domain.performance.dao.PerformanceHeartRepository;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.AlreadyExistException;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PerformanceHeartServiceTest {

    @InjectMocks private PerformanceHeartService performanceHeartService;
    @Mock private PerformanceHeartRepository performanceHeartRepository;
    @Mock private PerformanceRepository performanceRepository;
    @Mock private UserRepository userRepository;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    private Performance performance;
    private PerformanceHeart performanceHeart;
    private User user;

    @BeforeAll
    public static void beforeALl() {
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void afterAll() {
        mockedSecurityUtil.close();
    }

    @BeforeEach
    void setUp() {
        user = User.builder().build();
        ReflectionTestUtils.setField(user, "performanceHeartList", new ArrayList<>());
        performance = PerformanceTestHelper.createPerformance(1L, "Harry Styles");
        performanceHeart = PerformanceTestHelper.createPerformanceHeart(1L, user, performance);
    }

    @Nested
    @DisplayName("공연 좋아요 저장")
    class savePerformancesHeart {
        @Test
        @DisplayName("공연을 정상정으로 저장한다.")
        void savePerformanceHeartTest() {
            //given
            Long performanceId = 1L;
            Long userId = 1L;
            Long performanceHeartId = 1L;

            //mocking
            given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
            given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));
            given(performanceRepository.findById(performanceId)).willReturn(Optional.ofNullable(performance));
            given(performanceHeartRepository.findByUserAndPerformance(any(), any())).willReturn(Optional.empty());
            given(performanceHeartRepository.save(any())).willReturn(performanceHeart);

            //when
            Long saveId = performanceHeartService.save(performanceId);

            //then
            assertThat(performanceHeartId).isEqualTo(saveId);
        }

        @Test
        @DisplayName("존재하지 않는 공연이라면 공연 좋아요 저장에 실패한다.")
        void savePerformanceButNotExistPerformanceTest() {
            //given
            Long performanceId = 1L;
            Long userId = 1L;

            //mocking
            given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
            given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));
            given(performanceRepository.findById(performanceId)).willReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> performanceHeartService.save(performanceId))
                    .isInstanceOf(NotExistException.class)
                    .hasMessageContaining(PERFORMANCE_NOT_EXIST.getMessage());
        }

        @Test
        @DisplayName("회원이 로그인 상태가 아니라면 좋아요 저장에 실패한다.")
        void savePerformanceButNotLoginInTest() {
            Long performanceId = 1L;
            given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());
            assertThatThrownBy(() -> performanceHeartService.save(performanceId))
                    .isInstanceOf(NotExistException.class)
                    .hasMessageContaining(USER_NOT_EXIST.getMessage());
        }

        @Test
        @DisplayName("이미 좋아요를 누른 상태라면 좋아요 저장에 실패한다.")
        void savePerformanceButExistHeartTest(){
            //given
            Long performanceId = 1L;
            Long userId = 1L;

            //mocking
            given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
            given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));
            given(performanceRepository.findById(performanceId)).willReturn(Optional.ofNullable(performance));
            given(performanceHeartRepository.findByUserAndPerformance(any(), any())).willReturn(
                    Optional.ofNullable(performanceHeart));

            //then
            assertThatThrownBy(() -> performanceHeartService.save(performanceId))
                    .isInstanceOf(AlreadyExistException.class)
                    .hasMessageContaining(PERFORMANCE_HEART_ALREADY_EXIST.getMessage());
        }
    }

    @Nested
    @DisplayName("공연 좋아요 삭제")
    class deletePerformancesHeart {
        @Test
        @DisplayName("회원이 로그인 상태가 아니라면 좋아요 삭제에 실패한다.")
        void savePerformanceButNotLoginInTest() {
            Long performanceId = 1L;
            given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());
            assertThatThrownBy(() -> performanceHeartService.delete(performanceId))
                    .isInstanceOf(NotExistException.class)
                    .hasMessageContaining(USER_NOT_EXIST.getMessage());
        }

        @Test
        @DisplayName("존재하지 않는 공연이라면 공연 좋아요 삭제에 실패한다.")
        void savePerformanceButNotExistPerformanceTest() {
            //given
            Long performanceId = 1L;
            Long userId = 1L;

            //mocking
            given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
            given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));
            given(performanceRepository.findById(performanceId)).willReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> performanceHeartService.delete(performanceId))
                    .isInstanceOf(NotExistException.class)
                    .hasMessageContaining(PERFORMANCE_NOT_EXIST.getMessage());
        }

        @Test
        @DisplayName("좋아요한 공연이 없다면 좋아요 삭제에 실패한다.")
        void savePerformanceButNotExistHeartTest(){
            //given
            Long performanceId = 1L;
            Long userId = 1L;

            //mocking
            given(SecurityUtil.getCurrentUserId()).willReturn(Optional.of(userId));
            given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));
            given(performanceRepository.findById(performanceId)).willReturn(Optional.ofNullable(performance));
            given(performanceHeartRepository.findByUserAndPerformance(any(), any())).willReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> performanceHeartService.delete(performanceId))
                    .isInstanceOf(NotExistException.class)
                    .hasMessageContaining(PERFORMANCE_HEART_NOT_EXIST.getMessage());
        }

    }



}