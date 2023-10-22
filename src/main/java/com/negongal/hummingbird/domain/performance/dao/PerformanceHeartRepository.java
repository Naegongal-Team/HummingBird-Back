package com.negongal.hummingbird.domain.performance.dao;

import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceHeartRepository extends JpaRepository<PerformanceHeart, Long> {
}
