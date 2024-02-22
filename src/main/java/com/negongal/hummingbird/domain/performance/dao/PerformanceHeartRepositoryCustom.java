package com.negongal.hummingbird.domain.performance.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;

public interface PerformanceHeartRepositoryCustom {

	Page<PerformanceDto> findAllByUserHeart(Pageable pageable, Long userId);
}
