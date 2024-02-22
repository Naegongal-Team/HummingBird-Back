package com.negongal.hummingbird.domain.performance.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.negongal.hummingbird.domain.performance.dto.request.PerformanceSearchRequestDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;

public interface PerformanceRepositoryCustom {

	Page<PerformanceDto> findAllCustom(Pageable pageable);

	List<PerformanceDto> findSeveral(int size, String sort);

	List<PerformanceDto> findByArtist(String artistId, boolean scheduled);

	Page<PerformanceDto> search(PerformanceSearchRequestDto requestDto, Pageable pageable);
}
