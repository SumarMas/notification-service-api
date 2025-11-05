package com.platform.notification_service.services.Ngo.impl;

import com.platform.notification_service.dtos.Ngo.NgoStatusMessageDto;
import com.platform.notification_service.entities.NotificationEntity;
import com.platform.notification_service.enums.NotificationType;
import com.platform.notification_service.services.Ngo.INgoStatusService;
import com.platform.notification_service.services.email.IEmailService;
import com.platform.notification_service.services.notification.INotificationService;
import com.platform.notification_service.services.template.ITemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * Service implementation for handling NGO status notifications.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NgoStatusService implements INgoStatusService {
    /** Email service for sending notifications. */
    private final IEmailService emailService;
    /** Template service for rendering email templates. */
    private final ITemplateService templateService;
    /** Notification service for sending notifications. */
    private final INotificationService notificationService;
    /**
     * Sends a notification about the change in NGO status.
     *
     * @param ngoStatusMessageDto The DTO containing NGO status change information.
     */
    @Override
    public void sendNgoStatusChangeNotification(NgoStatusMessageDto ngoStatusMessageDto) {
        log.trace("Sending NGO status change notification for NGO: {} with status: {}",
                ngoStatusMessageDto.getNameNgo(), ngoStatusMessageDto.getStatus());
        switch (ngoStatusMessageDto.getStatus()) {
            case VERIFIED -> handleApprovedStatus(ngoStatusMessageDto);
            case DENIED -> handleRejectedStatus(ngoStatusMessageDto);
            case PENDING -> handlePendingStatus(ngoStatusMessageDto);
            default -> log.warn("Unhandled NGO status: {}", ngoStatusMessageDto.getStatus());
        }
    }

    private void handleApprovedStatus(NgoStatusMessageDto ngoStatusMessageDto) {
        sendApprovedEmail(ngoStatusMessageDto);
        String title = "NGO Approved";
        String message = "Congratulations! Your NGO \""
                + ngoStatusMessageDto.getNameNgo() + "\" has been approved.";
        sendInAppNotification(ngoStatusMessageDto, title, message, NotificationType.NGO_DOCUMENTS_APPROVED);
    }

    private void handleRejectedStatus(NgoStatusMessageDto ngoStatusMessageDto) {
        sendRejectedEmail(ngoStatusMessageDto);
        String title = "NGO Application Rejected";
        String message = "We regret to inform you that your NGO \""
                + ngoStatusMessageDto.getNameNgo() + "\" application has been rejected.";
        sendInAppNotification(ngoStatusMessageDto, title, message, NotificationType.NGO_DOCUMENTS_REJECTED);
    }

    private void handlePendingStatus(NgoStatusMessageDto ngoStatusMessageDto) {
        sendPendingEmail(ngoStatusMessageDto);
        String title = "NGO Application Pending";
        String message = "Your NGO \"" + ngoStatusMessageDto.getNameNgo()
                + "\" application is currently pending review.";
        sendInAppNotification(ngoStatusMessageDto, title, message, NotificationType.NGO_DOCUMENTS_RECEIVED);
    }

    private String renderTemplate(NgoStatusMessageDto ngoStatusMessageDto, String templateName) {
        Map<String, String> templateParams = Map.of(
                "firstName", ngoStatusMessageDto.getUser().getFirstName(),
                "organizationName", ngoStatusMessageDto.getNameNgo(),
                "comment", ngoStatusMessageDto.getMessage()
        );
        return templateService.render(templateName, templateParams);
    }

    private void sendApprovedEmail(NgoStatusMessageDto ngoStatusMessageDto) {
        String subject = "Your NGO Has Been Approved!";
        String body = renderTemplate(ngoStatusMessageDto, "organization-verified-template.html");
        emailService.send(ngoStatusMessageDto.getUser().getEmail(), null, subject, body);
    }

    private void sendRejectedEmail(NgoStatusMessageDto ngoStatusMessageDto) {
        String subject = "Your NGO Application Has Been Rejected";
        String body = renderTemplate(ngoStatusMessageDto, "organization-rejected-template.html");
        emailService.send(ngoStatusMessageDto.getUser().getEmail(), null, subject, body);
    }

    private void sendPendingEmail(NgoStatusMessageDto ngoStatusMessageDto) {
        String subject = "Your NGO Application is Pending Review";
        String body = renderTemplate(ngoStatusMessageDto, "organization-pending-template.html");
        emailService.send(ngoStatusMessageDto.getUser().getEmail(), null, subject, body);
    }

    private void sendInAppNotification(NgoStatusMessageDto ngoStatusMessageDto,
                                       String title, String message,
                                       NotificationType notificationType) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationId(UUID.randomUUID())
                .userId(UUID.fromString(ngoStatusMessageDto.getUser().getId()))
                .message(message)
                .title(title)
                .type(notificationType)
                .build();
        notificationService.createNotification(notificationEntity);
    }
}
