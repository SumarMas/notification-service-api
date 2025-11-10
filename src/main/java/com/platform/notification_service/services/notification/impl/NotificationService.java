package com.platform.notification_service.services.notification.impl;

import com.platform.notification_service.context.IContextService;
import com.platform.notification_service.controllers.manageExceptions.CustomException;
import com.platform.notification_service.dtos.NotificationDto;
import com.platform.notification_service.entities.NotificationEntity;
import com.platform.notification_service.repositories.NotificationRepository;
import com.platform.notification_service.services.notification.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing notifications.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    /** Repository for managing notification entities. */
    private final NotificationRepository notificationRepository;
    /** Service for accessing context information. */
    private final IContextService contextService;
    /**
     * Creates a new notification.
     *
     * @param notificationEntity The notification entity to be created.
     */
    @Override
    public void createNotification(NotificationEntity notificationEntity) {
        log.trace("Creating notification for user ID: {}", notificationEntity.getUserId());
        try {
            notificationRepository.save(notificationEntity);
        } catch (DataAccessException ex) {
            log.error("Error creating notification. Exception: {}", ex.getMessage(), ex);
            throw new CustomException("Failed to create notification", HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    /**
     * Retrieves notifications for the currently authenticated user.
     *
     * @return A list of NotificationDto objects
     * representing the user's notifications.
     */
    @Override
    public List<NotificationDto> getMyNotifications() {
        UUID userId = contextService.getUserId();
        log.trace("Getting notifications for current user ID: {}", userId);
        return getNotificationsForUser(userId);
    }

    /**
     * Retrieves notifications for a specific user.
     *
     * @param userId The UUID of the user whose notifications are to be retrieved.
     * @return A list of NotificationDto objects
     * representing the user's notifications.
     */
    @Override
    public List<NotificationDto> getNotificationsForUser(UUID userId) {
        log.trace("Getting notifications for user ID: {}", userId);
        List<NotificationEntity> notifications = new ArrayList<>();
        try {
            notifications.addAll(notificationRepository.findTop10ByUserIdOrderByReadAscCreatedDatetimeDesc(userId));
        } catch (DataAccessException ex) {
            log.error("Error retrieving notifications for user ID: {}. Exception: {}", userId, ex.getMessage(), ex);
        }
        return mapToDtoList(notifications);
    }

    /**
     * Marks a notification as read.
     *
     * @param notificationId The UUID of the notification to be marked as read.
     */
    @Override
    public void markNotificationAsRead(UUID notificationId) {
        log.trace("Marking notification ID: {} as read", notificationId);
        try {
            NotificationEntity notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new CustomException("Notification not found", HttpStatus.NOT_FOUND));
            notification.setRead(true);
            notificationRepository.save(notification);
        } catch (DataAccessException ex) {
            log.error("Error marking notification ID: {} as read. Exception: {}", notificationId, ex.getMessage(), ex);
        } catch (CustomException ex) {
            log.warn("Notification ID: {} not found.", notificationId);
        }
    }

    private List<NotificationDto> mapToDtoList(List<NotificationEntity> entities) {
        List<NotificationDto> dtos = new ArrayList<>();
        for (NotificationEntity entity : entities) {
            NotificationDto dto = NotificationDto.builder()
                    .notificationId(entity.getNotificationId())
                    .userId(entity.getUserId())
                    .message(entity.getMessage())
                    .read(entity.isRead())
                    .createdAt(entity.getCreatedDatetime())
                    .type(entity.getType())
                    .title(entity.getTitle())
                    .updatedAt(entity.getLastUpdatedDatetime())
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }
}
