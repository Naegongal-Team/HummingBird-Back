package com.negongal.hummingbird.domain.performance.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.performance.dao.PerformanceDateRepository;
import com.negongal.hummingbird.domain.performance.dao.PerformanceHeartRepository;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.performance.dto.TicketingRequestDto;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

    @InjectMocks private PerformanceService performanceService;

    @Mock private PerformanceRepository performanceRepository;
//    @Mock private PerformanceHeartRepository heartRepository;
//    @Mock private PerformanceDateRepository dateRepository;
//    @Mock private ArtistRepository artistRepository;
//    @Mock private UserRepository userRepository;

    public PerformanceRequestDto generatePerformance() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<LocalDateTime> dateList = new ArrayList<>();
        dateList.add(LocalDateTime.parse("2024-02-27 20:30", formatter));
        dateList.add(LocalDateTime.parse("2024-02-28 20:30", formatter));

        List<TicketingRequestDto> TicketingList = new ArrayList<>();
        TicketingList.add(TicketingRequestDto.builder()
                .startDate(LocalDateTime.parse("2024-01-30 20:30", formatter))
                .platform("http://m.ticket.yes24.com/Perf/46461")
                .link("예스24")
                .build());
        TicketingList.add(TicketingRequestDto.builder()
                .startDate(LocalDateTime.parse("2024-01-31 20:30", formatter))
                .platform("http://m.ticket.yes24.com/Perf/46461")
                .link("예스24")
                .build());

        return PerformanceRequestDto.builder()
                .name("Harry Styles 공연")
                .artistName("Harry Styles")
                .location("일산 킨텍스 제1전시장 4,5홀")
                .description("설명")
                .runtime(70L)
                .dateList(dateList)
                .regularTicketing(TicketingList)
                .build();
    }

    @Test
    @DisplayName("공연 저장")
    void savePerformance() {
        //given
        PerformanceRequestDto request = generatePerformance();
        Performance performance = request.toEntity(Artist.builder().build());
        given(performanceRepository.save(any(Performance.class))).willReturn(performance);

        //when
        String photo = "url";
        Long saverId = performanceService.save(request, photo);

        //then
        assertThat(saverId).isEqualTo(performance.getId());
    }

}