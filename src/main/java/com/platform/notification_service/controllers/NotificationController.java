package com.platform.notification_service.controllers;


import com.platform.notification_service.dtos.NotificationDto;
import com.platform.notification_service.services.notification.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for user in-app notifications.
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    /** Notification service for managing notifications. */
    private final INotificationService notificationService;
    /**
     * Retrieves notifications for the currently authenticated user.
     *
     * @return A ResponseEntity containing a list of NotificationDto objects
     * representing the user's notifications.
     */
    @GetMapping("/my-notifications")
    public ResponseEntity<List<NotificationDto>> getMyNotifications() {
        List<NotificationDto> notifications = notificationService.getMyNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Marks a notification as read.
     *
     * @param notificationId The UUID of the notification to be marked as read.
     * @return A ResponseEntity with no content.
     */
    @PatchMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable UUID notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}
