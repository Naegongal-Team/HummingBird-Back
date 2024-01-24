package com.negongal.hummingbird.domain.performance.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.Ticketing;
import com.negongal.hummingbird.global.common.BaseTimeEntity;
import com.negongal.hummingbird.global.config.QueryDSLConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDSLConfig.class, BaseTimeEntity.class})
class PerformanceRepositoryCustomImplTest {

    @Autowired
    PerformanceRepository performanceRepository;

    @BeforeEach
    void setUp() {
//        artist
//        Performance performance = Performance.builder()
//                .name("공연 1")
//                .artist(artist)
//                .location("location 1")
//                .runtime(120)
//                .description("description 1")
//                .build();
//
//        Ticketing.builder()
//                .performance(performance)
//                .ticketType(ticketType)
//                .startDate(startDate)
//                .platform(platform)
//                .link(link)
//                .build();

    }



}