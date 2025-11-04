package com.platform.notification_service.services.notification;

import com.platform.notification_service.dtos.NotificationDto;
import com.platform.notification_service.entities.NotificationEntity;

import java.util.List;
import java.util.UUID;
/**
 * Service interface for managing notifications.
 */
public interface INotificationService {
    /** Creates a new notification.
     *
     * @param notificationEntity The notification entity to be created.
     */
    void createNotification(NotificationEntity notificationEntity);
    /** Retrieves notifications for a specific user.
     *
     * @param userId The UUID of the user whose notifications are to be retrieved.
     * @return A list of NotificationDto objects representing the user's notifications.
     */
    List<NotificationDto> getNotificationsForUser(UUID userId);
    /** Marks a notification as read.
     *
     * @param notificationId The UUID of the notification to be marked as read.
     */
    void markNotificationAsRead(UUID notificationId);
}
