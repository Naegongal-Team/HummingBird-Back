package com.negongal.hummingbird.domain.performance.dao;

import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import com.negongal.hummingbird.domain.user.domain.User;
import java.util.Optional;
import java.util.logging.LoggingPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PerformanceHeartRepository extends JpaRepository<PerformanceHeart, Long>, PerformanceHeartRepositoryCustom {

    Optional<PerformanceHeart> findByUserAndPerformance(User user, Performance performance);
}
