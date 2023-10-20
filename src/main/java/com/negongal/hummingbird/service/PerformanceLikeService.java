package com.negongal.hummingbird.service;

import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.domain.PerformanceLike;
import com.negongal.hummingbird.repository.PerformanceLikeRepository;
import com.negongal.hummingbird.repository.PerformanceRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PerformanceLikeService {
    private final PerformanceLikeRepository performanceLikeRepository;
    private final PerformanceRepository performanceRepository;

    @Transactional
    public void save(Long performanceId) {

        /**
         * User 조회 + 매핑 필요
         */
        Long userId = 1L;

        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공연입니다."));

        PerformanceLike performanceLike = PerformanceLike.builder()
                .performance(performance)
                .userId(userId)
                .build();

        performanceLikeRepository.save(performanceLike);
    }
}
