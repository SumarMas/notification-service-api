package com.platform.notification_service.services.campaigns;

import com.platform.notification_service.dtos.campaign.CampaignClosedEventDto;
/**
 * Service interface for handling campaign close notifications.
 */
public interface ICampaignCloseService {
    /**
     * Sends a notification when a campaign is closed.
     *
     * @param campaignClosedEventDto The DTO containing details about
     *                               the closed campaign.
     */
    void sendCampaignCloseNotification(CampaignClosedEventDto campaignClosedEventDto);
}
