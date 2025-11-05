package com.platform.notification_service.restClients.campaigns;

import com.platform.notification_service.dtos.campaign.CampaignDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * REST client interface for interacting with the Campaign service.
 */
public interface ICampaignRestClient {
    /**
     * Retrieves a CampaignDto by its unique identifier.
     *
     * @param campaignId The unique identifier of the campaign.
     * @return A ResponseEntity containing the CampaignDto
     * corresponding to the provided ID.
     */
    ResponseEntity<CampaignDto> getCampaignById(UUID campaignId);
}
