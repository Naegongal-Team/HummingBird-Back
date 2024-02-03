package com.negongal.hummingbird.domain.performance.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.chat.dao.ChatRoomRepository;
import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import com.negongal.hummingbird.domain.performance.PerformanceTestHelper;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceDate;
import com.negongal.hummingbird.domain.performance.domain.Ticketing;
import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import com.negongal.hummingbird.global.common.BaseTimeEntity;
import com.negongal.hummingbird.global.config.JpaConfig;
import com.negongal.hummingbird.global.config.QueryDSLConfig;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDSLConfig.class, JpaConfig.class})
@DataJpaTest
class PerformanceRepositoryTest {

    @Autowired PerformanceRepository performanceRepository;
    @Autowired ArtistRepository artistRepository;
    @Autowired PerformanceDateRepository performanceDateRepository;
    @Autowired TicketingRepository ticketingRepository;

    @BeforeEach
    void setUp() {
        String[] artistName = PerformanceTestHelper.getArtistNames();
        String[][] dates = PerformanceTestHelper.getDates();
        String[][] ticketingDates = PerformanceTestHelper.getTicketingDates();
        for(int i = 0; i < 5; i++) {
            Artist artist = Artist.builder()
                    .id(artistName[i] + "ID123")
                    .name(artistName[i])
                    .image("image")
                    .popularity(100)
                    .heartCount(10)
                    .performanceList(new ArrayList<>())
                    .build();
            artistRepository.save(artist);

            Performance performance = Performance.builder()
                    .name(artist.getName() + "의 콘서트 " + i)
                    .artist(artist)
                    .location("서울 월드컵 경기장")
                    .runtime(60L)
                    .photo("image")
                    .build();
            performanceRepository.save(performance);

            List<PerformanceDate> dateList = PerformanceTestHelper.createPerformanceDateList(performance, dates[i]);
            performanceDateRepository.saveAll(dateList);

            List<Ticketing> ticketingList = PerformanceTestHelper.createTicketingList(performance, ticketingDates[i]);
            ticketingRepository.saveAll(ticketingList);
        }
    }

    @Test
    @DisplayName("공연 시작 날짜가 지난 경우는 조회되지 않는다.")
    void findPerformancesButStartDatePassedTest() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<PerformanceDto> dtoPage = performanceRepository.findAllCustom(pageable);
        List<PerformanceDto> content = dtoPage.getContent();

        for (PerformanceDto p : content) {
            System.out.println(p.getDate());
        }

        assertThat(dtoPage.getTotalElements()).isEqualTo(4);
        for(int i = 0; i < content.size(); i++) {
            assertThat(content.get(i).getDate()).isAfterOrEqualTo(LocalDateTime.now());
        }
    }

    @Test
    @DisplayName("정렬 조건이 없다면 공연 시작 날짜를 기준으로 조회한다.")
    void findPerformancesButNotSortConditionTest() {
        Pageable pageable = PageRequest.of(0, 5);

        Page<PerformanceDto> dtoPage = performanceRepository.findAllCustom(pageable);
        List<PerformanceDto> content = dtoPage.getContent();

        for(int i = 0; i < content.size() - 1; i++) {
            assertThat(content.get(i).getDate()).isBeforeOrEqualTo(content.get(i + 1).getDate());
        }
    }

    @Test
    @DisplayName("티켓팅 날짜 기준으로 공연 조회를 성공한다.")
    void findPerformancesByTicketingDateTest() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("ticketing"));

        Page<PerformanceDto> dtoPage = performanceRepository.findAllCustom(pageable);
        List<PerformanceDto> content = dtoPage.getContent();

        assertThat(content.get(0).getArtistName()).isEqualTo("Slowdives");
        assertThat(content.get(content.size() - 1).getArtistName()).isEqualTo("Novo Amors");
    }

    @Test
    @DisplayName("가수의 공연 조회 시, scheduled 조건에 따라 조회에 성공한다.")
    void findArtistPerformancesButStartDatePassedTest() {
        String findArtistId = "Harry StylessID123";
        List<PerformanceDto> scheduledPerformances = performanceRepository.findByArtist(findArtistId, true);
        List<PerformanceDto> pastPerformances = performanceRepository.findByArtist(findArtistId, false);

        assertThat(scheduledPerformances.size()).isEqualTo(1);
        assertThat(pastPerformances.size()).isEqualTo(1);
    }

}