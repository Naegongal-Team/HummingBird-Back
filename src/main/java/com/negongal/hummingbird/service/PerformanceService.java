package com.negongal.hummingbird.service;

import com.negongal.hummingbird.api.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    @Transactional
    public Long save(PerformanceRequestDto requestDto){

        Performance performance = Performance.createPerformanceBuilder()
                .name(requestDto.getName())
                .location(requestDto.getLocation())
                .runtime(requestDto.getRuntime())
                .date(requestDto.getDate())
                .build();

        if(requestDto.getDescription() != null) {
            performance.addDescription(requestDto.getDescription());
        }

        /**
         * 티켓팅 정보 - 날짜, 링크 추가 필요
         */

        performanceRepository.save(performance);
        return performance.getId();
    }
}
