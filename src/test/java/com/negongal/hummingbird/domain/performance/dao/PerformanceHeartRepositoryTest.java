//package com.negongal.hummingbird.domain.performance.dao;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
//import com.negongal.hummingbird.domain.artist.domain.Artist;
//import com.negongal.hummingbird.domain.performance.PerformanceTestHelper;
//import com.negongal.hummingbird.domain.performance.domain.Performance;
//import com.negongal.hummingbird.domain.performance.domain.PerformanceDate;
//import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
//import com.negongal.hummingbird.domain.performance.domain.Ticketing;
//import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
//import com.negongal.hummingbird.domain.user.dao.UserRepository;
//import com.negongal.hummingbird.domain.user.domain.Role;
//import com.negongal.hummingbird.domain.user.domain.User;
//import com.negongal.hummingbird.global.config.JpaConfig;
//import com.negongal.hummingbird.global.config.QueryDSLConfig;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import({QueryDSLConfig.class, JpaConfig.class})
//@DataJpaTest
//class PerformanceHeartRepositoryTest {
//
//    @Autowired PerformanceHeartRepository performanceHeartRepository;
//    @Autowired PerformanceRepository performanceRepository;
//    @Autowired ArtistRepository artistRepository;
//    @Autowired PerformanceDateRepository performanceDateRepository;
//    @Autowired TicketingRepository ticketingRepository;
//    @Autowired UserRepository userRepository;
//
//    private Long userId;
//
//    @BeforeEach
//    void setUp() {
//        User user = User.builder()
//                .oauth2Id("oauth2Id")
//                .nickname("jim")
//                .provider("provider")
//                .role(Role.USER)
//                .build();
//       userId = userRepository.save(user).getUserId();
//
//        String[] artistName = PerformanceTestHelper.getArtistNames();
//        String[][] dates = PerformanceTestHelper.getDates();
//        String[][] ticketingDates = PerformanceTestHelper.getTicketingDates();
//        for(int i = 0; i < 5; i++) {
//            Artist artist = Artist.builder()
//                    .id(artistName[i] + "ID123")
//                    .name(artistName[i])
//                    .image("image")
//                    .build();
//            artistRepository.save(artist);
//
//            Performance performance = Performance.builder()
//                    .name(artist.getName() + "의 콘서트 " + i)
//                    .artist(artist)
//                    .location("서울 월드컵 경기장")
//                    .runtime(60L)
//                    .photo("image")
//                    .build();
//            performanceRepository.save(performance);
//
//            List<PerformanceDate> dateList = PerformanceTestHelper.createPerformanceDateList(performance, dates[i]);
//            performanceDateRepository.saveAll(dateList);
//
//            List<Ticketing> ticketingList = PerformanceTestHelper.createTicketingList(performance, ticketingDates[i]);
//            ticketingRepository.saveAll(ticketingList);
//
//            if(i % 2 == 0) { // 짝수만 좋아요 저장
//                PerformanceHeart performanceHeart = PerformanceHeart.builder()
//                        .performance(performance)
//                        .user(user)
//                        .build();
//                performanceHeartRepository.save(performanceHeart);
//            }
//        }
//    }
//
//    @Test
//    @DisplayName("유저가 좋아요한 공연만 조회된다")
//    void findAllPerformanceByUserHeartTest() {
//        Pageable pageable = PageRequest.of(0, 5);
//
//        Page<PerformanceDto> dtoPage = performanceHeartRepository.findAllByUserHeart(pageable, userId);
//        List<PerformanceDto> content = dtoPage.getContent();
//
//        assertThat(dtoPage.getTotalElements()).isEqualTo(3);
//        for (PerformanceDto p : content) {
//            assertThat(p.getName()).containsAnyOf(new String[]{"0", "2", "4"});
//        }
//    }
//}