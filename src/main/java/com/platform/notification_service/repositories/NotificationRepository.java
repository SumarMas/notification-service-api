package com.platform.notification_service.repositories;

import com.platform.notification_service.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Notification entities.
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    /** Find top 10 notifications by user ID, ordered by read status and creation date. */
    List<NotificationEntity> findTop10ByUserIdOrderByReadAscCreatedDatetimeDesc(UUID userId);
}
