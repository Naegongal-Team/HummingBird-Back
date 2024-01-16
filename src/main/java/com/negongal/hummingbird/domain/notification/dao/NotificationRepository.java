package com.negongal.hummingbird.domain.notification.dao;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.notification.domain.Notification;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findNotificationByNotificationTime(LocalDateTime notificationTime);

    Optional<Notification> findNotificationByUserAndPerformance(User user, Performance performance);
}
