package com.platform.notification_service.services.campaigns.impl;

import com.platform.notification_service.controllers.manageExceptions.CustomException;
import com.platform.notification_service.dtos.campaign.CampaignDto;
import com.platform.notification_service.restClients.campaigns.ICampaignRestClient;
import com.platform.notification_service.services.campaigns.ICampaignGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;
/**
 * Implementation of the ICampaignGetService interface
 * for retrieving campaign details.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignGetService implements ICampaignGetService {
    /** REST client for campaign-related operations. */
    private ICampaignRestClient campaignRestClient;
    /**
     * Retrieves a CampaignDto by its unique identifier.
     *
     * @param campaignId The unique identifier of the campaign.
     * @return The CampaignDto corresponding to the provided ID.
     */
    @Override
    public CampaignDto getCampaignById(UUID campaignId) {
        log.trace("getCampaignById campaignId={}", campaignId);
        try {
            CampaignDto campaignDto = campaignRestClient.getCampaignById(campaignId).getBody();
            if (campaignDto == null) {
                log.trace("No campaign found with ID: {}", campaignId);
                throw new CustomException("Campaign not found", HttpStatus.NOT_FOUND);
            }
            return  campaignDto;
        } catch (Exception e) {
            log.error("Error retrieving campaign with ID: {}", campaignId, e);
            throw e;
        }
    }
}
