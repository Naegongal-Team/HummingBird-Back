package com.negongal.hummingbird.domain.performance.dao;

import com.negongal.hummingbird.domain.performance.domain.PerformanceDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceDateRepository extends JpaRepository<PerformanceDate, Long> {
}
