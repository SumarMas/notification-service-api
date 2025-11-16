package com.platform.notification_service.services.payout.impl;

import com.platform.notification_service.dtos.Ngo.NgoDto;
import com.platform.notification_service.dtos.payout.PayoutMessageDto;
import com.platform.notification_service.dtos.user.UserDto;
import com.platform.notification_service.entities.NotificationEntity;
import com.platform.notification_service.enums.NotificationType;
import com.platform.notification_service.services.Ngo.INgoGetService;
import com.platform.notification_service.services.email.IEmailService;
import com.platform.notification_service.services.notification.INotificationService;
import com.platform.notification_service.services.payout.IPayoutPaidService;
import com.platform.notification_service.services.template.ITemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutPaidService implements IPayoutPaidService {
    /** Service for retrieving NGO details. */
    private final INgoGetService ngoGetService;
    /** Template service for rendering email templates. */
    private final ITemplateService templateService;
    /** Email service for sending notifications. */
    private final IEmailService emailService;
    /** Notification service for sending notifications. */
    private final INotificationService notificationService;
    /**
     * Sends a notification when a payout has been paid.
     *
     * @param payoutMessageDto the payout message data transfer object
     */
    @Override
    public void sendPayoutPaidNotification(PayoutMessageDto payoutMessageDto) {
        log.trace("sendPayoutPaidNotification payoutMessageDto={}", payoutMessageDto);
        NgoDto ngoDto = getNgoById(payoutMessageDto.getNgoId());
        appNotifyPayoutPaid(ngoDto, payoutMessageDto);
        sendPayoutPaidEmail(ngoDto, payoutMessageDto);
    }

    private NgoDto getNgoById(UUID id) {
        return ngoGetService.getById(id);
    }

    private void appNotifyPayoutPaid(NgoDto ngoDto, PayoutMessageDto payoutMessageDto) {
       String title = "Payout Paid";
       String message = String.format("Your payout of $%s has been paid.", payoutMessageDto.getTotalAmount());
       sendInAppNotification(ngoDto.getUserCreator(), title, message, NotificationType.PAYOUT_APPROVED);
    }

    private void sendPayoutPaidEmail(NgoDto ngoDto, PayoutMessageDto payoutMessageDto) {
        String subject = "Payout Paid";
        String templateName = "payout_paid_template.html";
        String body = renderTemplate(ngoDto, payoutMessageDto, templateName);
        emailService.send(ngoDto.getUserCreator().getEmail(), null, subject, body);
    }

    private String renderTemplate(NgoDto ngoDto, PayoutMessageDto payoutMessageDto, String templateName) {
        Map<String, String> templateParams = Map.of(
                "id", payoutMessageDto.getPayoutId().toString(),
                "organizationName", ngoDto.getName(),
                "totalAmount", payoutMessageDto.getTotalAmount().toString(),
                "date", payoutMessageDto.getPayoutDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
        return templateService.render(templateName, templateParams);
    }

    private void sendInAppNotification(UserDto userDto,
                                       String title, String message,
                                       NotificationType notificationType) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationId(UUID.randomUUID())
                .userId(UUID.fromString(userDto.getId()))
                .message(message)
                .title(title)
                .type(notificationType)
                .build();
        notificationService.createNotification(notificationEntity);
    }
}
