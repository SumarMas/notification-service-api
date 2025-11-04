package com.platform.notification_service.entities;

import com.platform.notification_service.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entity representing an in-app notification.
 */
@Entity
@Table(name = "notifications")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity extends AuditEntity {
    /** Unique identifier for the notification. */
    @Id
    @Column(name = "notification_id", columnDefinition = "BINARY(16)")
    private UUID notificationId;
    /** Identifier of the user receiving the notification. */
    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;
    /** Title of the notification. */
    @Column(nullable = false)
    private String title;
    /** Message content of the notification. */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    /** Type of the notification */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;
    /** Flag indicating whether the notification has been read. */
    @Column(name = "is_read", nullable = false)
    private boolean read = false;
}
