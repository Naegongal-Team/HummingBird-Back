package com.negongal.hummingbird.repository;

import com.negongal.hummingbird.domain.PerformanceDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceDateRepository extends JpaRepository<PerformanceDate, Long> {
}
