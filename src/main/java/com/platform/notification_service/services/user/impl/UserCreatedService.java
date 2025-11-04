package com.platform.notification_service.services.user.impl;

import com.platform.notification_service.dtos.user.UserDto;
import com.platform.notification_service.entities.NotificationEntity;
import com.platform.notification_service.enums.NotificationType;
import com.platform.notification_service.services.email.IEmailService;
import com.platform.notification_service.services.notification.INotificationService;
import com.platform.notification_service.services.template.ITemplateService;
import com.platform.notification_service.services.user.IUserCreatedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service implementation for handling actions related to user creation events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserCreatedService implements IUserCreatedService {
    /** Email service for sending notifications */
    private final IEmailService emailService;
    /** Template service for rendering email templates */
    private final ITemplateService templateService;
    /** Notification service for sending notifications */
    private final INotificationService notificationService;

    /**
     * Sends a welcome message to the newly created user.
     *
     * @param userDto The DTO containing user information.
     */
    @Override
    @Transactional
    public void sendWelcomeMessage(UserDto userDto) {
        // For demonstration purposes, we log the welcome message.
        log.info("Welcome message sent to user: {} with email: {}", userDto.getFirstName(), userDto.getEmail());
        sendInAppNotification(userDto);
        sendEmailNotification(userDto);
    }

    private String renderTemplate(UserDto userDto) {
        Map<String, String> templateParams = new ConcurrentHashMap<>();
        templateParams.put("firstName", userDto.getFirstName());

        return templateService.render("welcome-template.html", templateParams);
    }

    private void sendEmailNotification(UserDto userDto) {
        String subject = "Welcome to Our Platform";
        String body = renderTemplate(userDto);
        emailService.send(userDto.getEmail(), null, subject, body);
    }
    private void sendInAppNotification(UserDto userDto) {
        String title = "Welcome!";
        String message = String.format("Welcome %s to our platform!", userDto.getFirstName());
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationId(UUID.randomUUID())
                .userId(UUID.fromString(userDto.getId()))
                .message(message)
                .title(title)
                .type(NotificationType.USER_CREATED)
                .build();
        notificationService.createNotification(notificationEntity);
    }

}
