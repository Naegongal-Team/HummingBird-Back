package com.negongal.hummingbird.repository;

import com.negongal.hummingbird.domain.Performance;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformanceRepositoryCustom {

    Page<Performance> findAll(Pageable pageable);
    List<Performance> findSeveral(int size);
}
