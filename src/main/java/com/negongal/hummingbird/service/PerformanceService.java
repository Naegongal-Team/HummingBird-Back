package com.negongal.hummingbird.service;

import com.negongal.hummingbird.api.dto.PerformanceDetailDto;
import com.negongal.hummingbird.api.dto.PerformanceDto;
import com.negongal.hummingbird.api.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.domain.PerformanceDate;
import com.negongal.hummingbird.repository.PerformanceDateRepository;
import com.negongal.hummingbird.repository.PerformanceRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        performanceRepository.save(performance);

        List<PerformanceDate> dateList = createDate(requestDto.getDateList(), performance);
        dateRepository.saveAll(dateList);

        return performance.getId();
    }

    public List<PerformanceDate> createDate(List<LocalDateTime> dateList, Performance performance) {
        return dateList.stream().map(d -> PerformanceDate.builder()
                    .performance(performance)
                    .startDate(d)
                    .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long performanceId, PerformanceRequestDto request, String photo) {
        Performance findPerformance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공연입니다."));

        findPerformance.update(request.getName(), request.getArtistName(), request.getLocation(), request.getRuntime(), request.getDescription());
        findPerformance.addPhoto(photo);
        dateRepository.saveAll(createDate(request.getDateList(), findPerformance));
    }

    /**
     * 공연 조회: 공연 날짜 순 or 티켓팅 날짜 순 or 인기있는 공연 순
     */
    public Page<PerformanceDto> findAll(Pageable pageable) {
        return performanceRepository.findAllCustom(pageable);
    }

    /**
     * 공연 조회: 메인 페이지에서 띄울 개수 제한
     */
    public List<PerformanceDto> findSeveral(int size) {
        return performanceRepository.findSeveral(size);
    }

    public PerformanceDetailDto findOne(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공연입니다."));
        return PerformanceDetailDto.of(performance);
    }
}
