package com.platform.notification_service.dtos.campaign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/** DTO representing an event when a campaign is closed. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampaignClosedEventDto {
    /** The unique identifier of the campaign. */
    @JsonProperty("campaign_id")
    private UUID campaignId;
    /** The unique identifier of the organization
     * associated with the campaign. */
    @JsonProperty("organization_id")
    private UUID organizationId;
    /** The title of the campaign. */
    @JsonProperty("title")
    private String title;
    /** The reason for closing the campaign. */
    @JsonProperty("reason")
    private String reason; // "goal_reached" or "end_date_reached"
}
