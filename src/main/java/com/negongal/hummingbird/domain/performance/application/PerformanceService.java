package com.negongal.hummingbird.domain.performance.application;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.performance.dto.PerformancePageDto;
import com.negongal.hummingbird.domain.performance.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.performance.dao.PerformanceDateRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.dto.PerformanceDetailDto;
import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import com.negongal.hummingbird.domain.performance.domain.PerformanceDate;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.time.LocalDateTime;
import java.util.List;
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
    private final ArtistRepository artistRepository;

    @Transactional
    public Long save(PerformanceRequestDto requestDto, String photo) {
        Artist artist = artistRepository.findByName(requestDto.getArtistName())
                .orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));

        Performance performance = requestDto.toEntity(artist);
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
                .orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));

        Artist artist = artistRepository.findByName(request.getArtistName())
                .orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));

        findPerformance.update(request.getName(), artist, request.getLocation(), request.getRuntime(), request.getDescription());
        findPerformance.addPhoto(photo);
        dateRepository.saveAll(createDate(request.getDateList(), findPerformance));
    }

    /**
     * 공연 조회: 공연 날짜 순 or 티켓팅 날짜 순 or 인기있는 공연 순
     */
    public PerformancePageDto findAll(Pageable pageable) {
        Page<PerformanceDto> dtoPage = performanceRepository.findAllCustom(pageable);
        return PerformancePageDto.builder()
                .performanceDto(dtoPage.getContent())
                .totalPages(dtoPage.getTotalPages())
                .totalElements(dtoPage.getTotalElements())
                .isLast(dtoPage.isLast())
                .currPage(dtoPage.getPageable().getPageNumber())
                .build();
    }

    /**
     * 공연 조회: 메인 페이지에서 띄울 개수 제한
     */
    public List<PerformanceDto> findSeveral(int size, String sort) {
        return performanceRepository.findSeveral(size, sort);
    }

    public PerformanceDetailDto findOne(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));
        return PerformanceDetailDto.of(performance);
    }
}
