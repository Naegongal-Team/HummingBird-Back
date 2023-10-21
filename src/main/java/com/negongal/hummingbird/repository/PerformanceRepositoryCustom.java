package com.negongal.hummingbird.repository;

import com.negongal.hummingbird.api.dto.PerformanceDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformanceRepositoryCustom {

    Page<PerformanceDto> findAllCustom(Pageable pageable);
    List<PerformanceDto> findSeveral(int size);
}
