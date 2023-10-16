package com.negongal.hummingbird.repository;

import com.negongal.hummingbird.domain.Performance;
import java.util.List;

public interface PerformanceRepositoryCustom {

    List<Performance> findAllOrderByStartDate();
}
