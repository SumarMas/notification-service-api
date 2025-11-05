package com.platform.notification_service.services.campaigns;

import com.platform.notification_service.dtos.campaign.CampaignDto;

import java.util.UUID;

/**
 * Service interface for retrieving campaign information.
 */
public interface ICampaignGetService {
    /**
     * Retrieves a CampaignDto by its unique identifier.
     *
     * @param campaignId The unique identifier of the campaign.
     * @return The CampaignDto corresponding to the provided ID.
     */
    CampaignDto getCampaignById(UUID campaignId);
}
