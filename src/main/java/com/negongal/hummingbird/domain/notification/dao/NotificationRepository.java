package com.negongal.hummingbird.domain.notification.dao;

import com.negongal.hummingbird.domain.notification.domain.Notification;
<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.List;
=======
>>>>>>> f5d1f4b (feat: Notification 관련 클래스 작성)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
<<<<<<< HEAD

    List<Notification> findNotificationByNotificationTime(LocalDateTime notificationTime);
=======
>>>>>>> f5d1f4b (feat: Notification 관련 클래스 작성)
}
