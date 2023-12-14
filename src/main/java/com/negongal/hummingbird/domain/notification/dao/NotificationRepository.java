package com.negongal.hummingbird.domain.notification.dao;

import com.negongal.hummingbird.domain.notification.domain.Notification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findNotificationByNotificationTime(LocalDateTime notificationTime);
}
