package com.negongal.hummingbird.domain.performance.dao;

import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformanceHeartRepositoryCustom {

    Page<PerformanceDto> findAllByUserHeart(Pageable pageable, Long userId);
}
