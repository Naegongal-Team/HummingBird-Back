package com.negongal.hummingbird.service;

import com.negongal.hummingbird.api.dto.PerformanceDto;
import com.negongal.hummingbird.api.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.repository.PerformanceRepository;
import java.util.List;
import java.util.stream.Collectors;
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

        /**
         * Artist 조회 + 매핑 필요
         * requestDto.getArtistName()
         */

        Performance performance = Performance.createPerformanceBuilder()
                .name(requestDto.getName())
                .artistName(requestDto.getArtistName())  /** Artist 매핑 필요 **/
                .location(requestDto.getLocation())
                .runtime(requestDto.getRuntime())
                .date(requestDto.getDate())
                .build();

        if(requestDto.getDescription() != null) {
            performance.addDescription(requestDto.getDescription());
        }

        /**
         * 티켓팅 정보 - 날짜, 링크 추가 필요
         * S3 이미지 저장 필요
         */

        performanceRepository.save(performance);
        return performance.getId();
    }

    public List<PerformanceDto> findAll() {
        return performanceRepository.findAll()
                .stream().map(p -> PerformanceDto.of(p))
                .collect(Collectors.toList());
    }
}
