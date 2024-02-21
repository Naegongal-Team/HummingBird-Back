package com.negongal.hummingbird.domain.performance.dao;

import com.negongal.hummingbird.domain.performance.dto.request.PerformanceSearchRequestDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformanceRepositoryCustom {

    Page<PerformanceDto> findAllCustom(Pageable pageable);
    List<PerformanceDto> findSeveral(int size, String sort);
    List<PerformanceDto> findByArtist(String artistId, boolean scheduled);
    Page<PerformanceDto> search(PerformanceSearchRequestDto requestDto, Pageable pageable);
}
