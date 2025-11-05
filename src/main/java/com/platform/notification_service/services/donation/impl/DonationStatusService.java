package com.platform.notification_service.services.donation.impl;

import com.platform.notification_service.dtos.Ngo.NgoStatusMessageDto;
import com.platform.notification_service.dtos.campaign.CampaignDto;
import com.platform.notification_service.dtos.donation.DonationMessageDto;
import com.platform.notification_service.dtos.user.UserDto;
import com.platform.notification_service.entities.NotificationEntity;
import com.platform.notification_service.enums.DonationStatus;
import com.platform.notification_service.enums.NotificationType;
import com.platform.notification_service.services.campaigns.ICampaignGetService;
import com.platform.notification_service.services.donation.IDonationStatusService;
import com.platform.notification_service.services.email.IEmailService;
import com.platform.notification_service.services.notification.INotificationService;
import com.platform.notification_service.services.template.ITemplateService;
import com.platform.notification_service.services.user.IUserGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DonationStatusService implements IDonationStatusService {
    /** Service for retrieving user details. */
    private final IUserGetService userGetService;
    /** Service for retrieving campaign details. */
    private final ICampaignGetService campaignGetService;
    /** Template service for rendering email templates. */
    private final ITemplateService templateService;
    /** Email service for sending notifications. */
    private final IEmailService emailService;
    /** Notification service for sending notifications. */
    private final INotificationService notificationService;
    /**
     * Sends a notification regarding the status of a donation.
     *
     * @param donationMessageDto The donation message DTO containing donation details.
     */
    @Override
    @Transactional
    public void sendDonationStatusNotification(DonationMessageDto donationMessageDto) {
        DonationStatus previousStatus = donationMessageDto.getPreviousDonationStatus();
        DonationStatus newStatus = donationMessageDto.getDonationStatus();
        if ((previousStatus.equals(DonationStatus.CREATED) || previousStatus.equals(DonationStatus.CANCELLED))
                && newStatus.equals(DonationStatus.CONFIRMED)) {
            // Get user to notify
            UserDto userDto = getUserById(donationMessageDto.getUserId());

            // Get campaign details with ngo details
            CampaignDto campaignDto = getCampaignById(donationMessageDto.getCampaignId());
            String title = "Donation Successful";
            String message = "Thank you for your donation of " + donationMessageDto.getAmount()
                    + " to the campaign \"" + campaignDto.getTitle() + "\".";
            sendInAppNotification(userDto, title, message, NotificationType.DONATION_SUCCESS);
            sendSuccessfulDonationEmail(campaignDto, userDto, donationMessageDto);
        } else {
            log.debug("Skipping notification for donation ID: {} with previous status: {} and new status: {}",
                    donationMessageDto.getDonationId(), previousStatus, newStatus);
        }
    }

    private UserDto getUserById(UUID userId) {
        return userGetService.getUserById(userId);
    }
    private CampaignDto getCampaignById(UUID campaignId) {
        return campaignGetService.getCampaignById(campaignId);
    }
    private void sendSuccessfulDonationEmail(CampaignDto campaignDto, UserDto userDto, DonationMessageDto donationMessageDto) {
        String subject = "Thank you for your donation to " + campaignDto.getTitle();
        String templateName = "donation-template.html";
        String body = renderTemplate(campaignDto, userDto, donationMessageDto, templateName);
        log.debug("Sending successful donation email to: {}", userDto.getEmail());
        emailService.send(userDto.getEmail(), null, subject, body);
    }
    private String renderTemplate(CampaignDto campaignDto, UserDto userDto, DonationMessageDto donationMessageDto,String templateName) {
        Map<String, String> templateParams = Map.of(
                "campaign_name", campaignDto.getTitle(),
                "donor", userDto.getFirstName() + " " + userDto.getLastName(),
                "amount", donationMessageDto.getAmount().toString(),
                "operation_id", donationMessageDto.getDonationId().toString()
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
