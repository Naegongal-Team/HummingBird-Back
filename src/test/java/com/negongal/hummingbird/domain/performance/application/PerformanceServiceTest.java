package com.negongal.hummingbird.domain.performance.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.notification.application.NotificationService;
import com.negongal.hummingbird.domain.performance.PerformanceTestHelper;
import com.negongal.hummingbird.domain.performance.dao.PerformanceDateRepository;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDetailDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformancePageDto;
import com.negongal.hummingbird.domain.performance.dto.request.PerformanceRequestDto;
import com.negongal.hummingbird.domain.performance.dto.request.PerformanceSearchRequestDto;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

    @InjectMocks private PerformanceService performanceService;
    @Mock private PerformanceRepository performanceRepository;
    @Mock private PerformanceDateRepository dateRepository;
    @Mock private ArtistRepository artistRepository;
    @Mock private NotificationService notificationService;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    private Performance performance;

    @BeforeAll
    static void beforeALl() {
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    static void afterAll() {
        mockedSecurityUtil.close();
    }

    @BeforeEach
    void setUp() {
        performance = PerformanceTestHelper.createPerformance(1L, "Harry Styles");
    }

    @Nested
    @DisplayName("공연 다중 조회")
    class findPerformances {
        @Test
        @DisplayName("전체 공연 조회 시, 페이지네이션으로 공연 리스트 조회를 성공한다.")
        void findPerformancesByPaginationTest() {
            //given
            Performance performance2 = PerformanceTestHelper.createPerformance(2L, "Jeff Bernat");
            Pageable pageable = PageRequest.of(0, 5);
            Page<PerformanceDto> dtoPage = new PageImpl(Arrays.asList(performance, performance2), pageable, 2);

            // mocking
            given(performanceRepository.findAllCustom(any(Pageable.class))).willReturn(dtoPage);

            //when
            PerformancePageDto performancePageDto = performanceService.findAllPerformance(pageable);

            //then
            assertThat(dtoPage.getTotalElements()).isEqualTo(performancePageDto.getTotalElements());
            assertThat(dtoPage.getContent().size()).isEqualTo(performancePageDto.getPerformanceDtos().size());
            assertThat(performancePageDto.isLast()).isEqualTo(true);
        }
    }

    @Nested
    @DisplayName("공연 단일 조회")
    class findOnePerformance {
        @Test
        @DisplayName("로그인을 하지 않으면 좋아요와 알람 여부는 false이다.")
        void findPerformanceButNotLogInTest() {
            //given
            Long findId = 1L;

            // mocking
            given(SecurityUtil.getCurrentUserId()).willReturn(Optional.empty());
            given(performanceRepository.findById(findId)).willReturn(Optional.ofNullable(performance));

            //when
            PerformanceDetailDto detailDto = performanceService.findPerformance(findId);

            //then
            assertThat(detailDto.getId()).isEqualTo(findId);
            assertThat(detailDto.isHeartPressed()).isEqualTo(false);
            assertThat(detailDto.isAlarmed()).isEqualTo(false);
        }

        @Test
        @DisplayName("아이디 값이 존재하지 않으면 에러가 발생한다")
        void findPerformanceButNotExistIdTest() {
            Long performanceId = 1L;
            given(performanceRepository.findById(any())).willReturn(Optional.empty());
            assertThatThrownBy(() -> performanceService.findPerformance(performanceId));
        }
    }

    @Nested
    @DisplayName("공연 검색")
    class searchPerformance {
        @Test
        @DisplayName("가수 이름으로 공연 검색에 성공한다.")
        void searchPerformanceByArtistNameTest() {
            //given
            PerformanceSearchRequestDto requestDto = PerformanceSearchRequestDto.builder().artistName("Harry Styles").build();
            Pageable pageable = PageRequest.of(0, 10);
            Page<PerformanceDto> dtoPage = new PageImpl(List.of(PerformanceDto.of(performance)), pageable, 1);

            // mocking
            given(performanceRepository.search(any(PerformanceSearchRequestDto.class), any(Pageable.class)))
                    .willReturn(dtoPage);

            //when
            PerformancePageDto performancePageDto = performanceService.searchPerformances(requestDto, pageable);

            //then
            assertThat(dtoPage.getTotalElements()).isEqualTo(performancePageDto.getTotalElements());
            assertThat(dtoPage.getContent().size()).isEqualTo(performancePageDto.getPerformanceDtos().size());
            assertThat(requestDto.getArtistName()).isEqualTo(performancePageDto.getPerformanceDtos().get(0).getArtistName());
        }
    }

    @Nested
    @DisplayName("공연 저장")
    class savePerformance {
        @Test
        @DisplayName("공연을 정상적으로 저장한다.")
        void savePerformanceTest() {
            //given
            PerformanceRequestDto request = PerformanceRequestDto.builder()
                    .name("Harry Styles 공연")
                    .artistName("Harry Styles")
                    .dates(new ArrayList<>())
                    .regularTicketing(new ArrayList<>())
                    .build();
            Artist artist = Artist.builder().name(request.getArtistName()).build();

            // mocking
            given(performanceRepository.save(any(Performance.class))).willReturn(performance);
            given(artistRepository.findByName(any(String.class))).willReturn(Optional.ofNullable(artist));

            //when
            Long saverId = performanceService.savePerformance(request, "");

            //then
            assertThat(performance.getId()).isEqualTo(saverId);
        }

        @Test
        @DisplayName("존재하지 않는 아티스트라면 공연 저장에 실패한다.")
        void savePerformanceButNotExistArtistTest() {
            PerformanceRequestDto request = PerformanceRequestDto.builder().build();
            given(artistRepository.findByName(any())).willReturn(Optional.empty());
            assertThatThrownBy(() -> performanceService.savePerformance(request, ""))
                    .isInstanceOf(NotExistException.class);
        }
    }

    @Nested
    @DisplayName("공연 수정")
    class updatePerformance {
        @Test
        @DisplayName("공연 아이디 값이 존재하지 않으면 공연 수정에 실패한다.")
        void updatePerformanceButNotPerformanceTest() {
            Long performanceId = 1L;
            PerformanceRequestDto request = PerformanceRequestDto.builder().build();
            given(performanceRepository.findById(any())).willReturn(Optional.empty());
            assertThatThrownBy(() -> performanceService.updatePerformance(performanceId, request, ""));
        }
    }

    @Nested
    @DisplayName("공연 삭제")
    class deletePerformance {
        @Test
        @DisplayName("공연 아이디 값이 존재하지 않으면 공연 삭제에 실패한다.")
        void deletePerformanceButNotPerformanceTest() {
            Long performanceId = 1L;
            given(performanceRepository.findById(any())).willReturn(Optional.empty());
            assertThatThrownBy(() -> performanceService.deletePerformance(performanceId));
        }
    }

}
