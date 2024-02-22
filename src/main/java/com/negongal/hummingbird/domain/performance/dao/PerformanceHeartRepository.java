package com.negongal.hummingbird.domain.performance.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import com.negongal.hummingbird.domain.user.domain.User;

public interface PerformanceHeartRepository
	extends JpaRepository<PerformanceHeart, Long>, PerformanceHeartRepositoryCustom {

	Optional<PerformanceHeart> findByUserAndPerformance(User user, Performance performance);
}
