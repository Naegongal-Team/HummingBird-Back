package com.negongal.hummingbird.domain.performance.dao;

import com.negongal.hummingbird.domain.performance.domain.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long>, PerformanceRepositoryCustom {
}
