package com.platform.notification_service.dtos.Ngo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.notification_service.dtos.user.UserDto;
import com.platform.notification_service.enums.NgoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the status message of an NGO.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NgoStatusMessageDto {
    /** The user associated with the NGO. */
    @JsonProperty("user")
    private UserDto user;
    /** The name of the NGO. */
    @JsonProperty("name_ngo")
    private String nameNgo;
    /** The status of the NGO. */
    @JsonProperty("status")
    private NgoStatus status;
    /** Additional message related to the NGO status. */
    @JsonProperty("message")
    private String message;
}
