package com.negongal.hummingbird.domain.performance.application;

import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import com.negongal.hummingbird.domain.performance.dao.PerformanceHeartRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PerformanceHeartService {
    private final PerformanceHeartRepository performanceHeartRepository;
    private final PerformanceRepository performanceRepository;

    @Transactional
    public void save(Long performanceId) {

        /**
         * User 조회 + 매핑 필요
         */
        Long userId = 1L;

        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공연입니다."));

        PerformanceHeart performanceHeart = PerformanceHeart.builder()
                .performance(performance)
                .userId(userId)
                .build();

        performanceHeartRepository.save(performanceHeart);
    }
}
