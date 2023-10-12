package com.negongal.hummingbird.service;

import com.negongal.hummingbird.api.dto.PerformanceDto;
import com.negongal.hummingbird.api.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.domain.PerformanceDate;
import com.negongal.hummingbird.repository.PerformanceDateRepository;
import com.negongal.hummingbird.repository.PerformanceRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    private final PerformanceDateRepository dateRepository;

    @Transactional
    public Long save(PerformanceRequestDto requestDto, String photo){

        /**
         * Artist 조회 + 매핑 필요
         * requestDto.getArtistName()
         */

        Performance performance = requestDto.toEntity();
        performance.addPhoto(photo);

        /**
         * S3 이미지 저장 필요
         */

        performanceRepository.save(performance);
        List<PerformanceDate> dateList = requestDto.getDate().stream()
                .map(d -> PerformanceDate.builder().performance(performance).date(d).build())
                .collect(Collectors.toList());
        dateRepository.saveAll(dateList);
        return performance.getId();
    }

    public List<PerformanceDto> findAll() {
        return performanceRepository.findAll()
                .stream().map(p -> PerformanceDto.of(p))
                .collect(Collectors.toList());
    }

    public PerformanceDto findOne(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공연입니다."));
        return PerformanceDto.of(performance);
    }
}
