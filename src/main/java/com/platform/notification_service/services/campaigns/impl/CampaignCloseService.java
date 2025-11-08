package com.platform.notification_service.services.campaigns.impl;

import com.platform.notification_service.dtos.campaign.CampaignClosedEventDto;
import com.platform.notification_service.dtos.campaign.CampaignDto;
import com.platform.notification_service.dtos.user.UserDto;
import com.platform.notification_service.entities.NotificationEntity;
import com.platform.notification_service.enums.NotificationType;
import com.platform.notification_service.services.campaigns.ICampaignCloseService;
import com.platform.notification_service.services.campaigns.ICampaignGetService;
import com.platform.notification_service.services.email.IEmailService;
import com.platform.notification_service.services.notification.INotificationService;
import com.platform.notification_service.services.template.ITemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.UUID;
/**
 * Service implementation for handling campaign close notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignCloseService implements ICampaignCloseService {
    /** Reason constants for campaign closure. */
    private static final String GOAL_REACHED = "goal_reached";
    /** Reason constants for campaign closure. */
    private static final String END_DATE_REACHED = "end_date_reached";
    /** Service for retrieving campaign details. */
    private final ICampaignGetService campaignGetService;
    /** Template service for rendering email templates. */
    private final ITemplateService templateService;
    /** Email service for sending notifications. */
    private final IEmailService emailService;
    /** Notification service for sending notifications. */
    private final INotificationService notificationService;
    /**
     * Sends a notification when a campaign is closed.
     *
     * @param campaignClosedEventDto The DTO containing details about
     *                               the closed campaign.
     */
    @Override
    @Transactional
    public void sendCampaignCloseNotification(CampaignClosedEventDto campaignClosedEventDto) {
        log.trace("Sending campaign close notification");
        CampaignDto campaignDto = getCampaignById(campaignClosedEventDto.getCampaignId());
        if (GOAL_REACHED.equals(campaignClosedEventDto.getReason())) {
            appNotifyCampaignClosedByGoalReached(campaignDto);
            sendCampaignClosedEmailByGoalReached(campaignDto);
        } else if (END_DATE_REACHED.equals(campaignClosedEventDto.getReason())) {
            appNotifyCampaignClosedByEndDateReached(campaignDto);
            sendCampaignClosedEmailByEndDateReached(campaignDto);
        } else {
            log.info("Campaign close by other reason, no notification sent.");
        }
    }

    private void sendCampaignClosedEmailByGoalReached(CampaignDto campaignDto) {
        String subject = "Campaign Closed: Goal Reached!";
        String templateName = "goal-reached-template.html";
        String body = renderTemplate(templateName, campaignDto);
        String recipientEmail = campaignDto.getNgo().getUserCreator().getEmail();
        emailService.send(recipientEmail, null, subject, body);
    }

    private void sendCampaignClosedEmailByEndDateReached(CampaignDto campaignDto) {
        String subject = "Campaign Closed: End Date Reached";
        String templateName = "campaign-ended-template.html";
        String body = renderTemplate(templateName, campaignDto);
        String recipientEmail = campaignDto.getNgo().getUserCreator().getEmail();
        emailService.send(recipientEmail, null, subject, body);
    }

    private void appNotifyCampaignClosedByGoalReached(CampaignDto campaignDto) {
        String title = "Campaign Closed: Goal Reached!";
        String message = "Congratulations! Your campaign \"" + campaignDto.getTitle()
                + "\" has been successfully closed after reaching its goal amount of "
                + campaignDto.getGoalAmount() + ".";
        sendInAppNotification(
                campaignDto.getNgo().getUserCreator(), title, message, NotificationType.CAMPAIGN_FINALIZED);
    }

    private void appNotifyCampaignClosedByEndDateReached(CampaignDto campaignDto) {
        String title = "Campaign Closed: End Date Reached";
        String message = "Your campaign \"" + campaignDto.getTitle()
                + "\" has been closed as it reached its end date on "
                + campaignDto.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".";
        sendInAppNotification(
                campaignDto.getNgo().getUserCreator(), title, message, NotificationType.CAMPAIGN_FINALIZED);
    }

    private CampaignDto getCampaignById(UUID campaignId) {
        return campaignGetService.getCampaignById(campaignId);
    }

    private String renderTemplate(String templateName, CampaignDto campaignDto) {
        Map<String, String> templateParams = Map.of(
                "organizationName", campaignDto.getNgo().getName(),
                "campaignName", campaignDto.getTitle(),
                "totalAmount", campaignDto.getCurrentAmount().toString(),
                "endDate", campaignDto.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
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
