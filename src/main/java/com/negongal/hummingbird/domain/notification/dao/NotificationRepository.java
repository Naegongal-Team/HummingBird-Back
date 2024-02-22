package com.negongal.hummingbird.domain.notification.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.negongal.hummingbird.domain.notification.domain.Notification;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.user.domain.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	@Query("SELECT n FROM Notification n WHERE n.notificationTime = :notificationTime")
	List<Notification> findByNotificationTime(@Param("notificationTime") LocalDateTime notificationTime);

	Optional<Notification> findByUserAndPerformance(User user, Performance performance);
}
