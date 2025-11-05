package com.platform.notification_service.restClients.ngos;

import com.platform.notification_service.dtos.Ngo.NgoDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
/**
 * REST client interface for interacting with the NGO service.
 */
public interface INgoRestClient {
    /**
     * Retrieves the NGO information associated with the current user.
     *
     * @param ngoId the unique identifier of the NGO
     * @return a ResponseEntity containing the NgoDto if found,
     * or an appropriate error response if no NGO is associated with the user
     */
    ResponseEntity<NgoDto> getNgoById(UUID ngoId);
}
