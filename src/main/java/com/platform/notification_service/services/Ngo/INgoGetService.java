package com.platform.notification_service.services.Ngo;

import com.platform.notification_service.dtos.Ngo.NgoDto;

import java.util.UUID;
/**
 * Service interface for retrieving NGO details.
 */
public interface INgoGetService {
    /** Retrieve NGO details by its unique identifier.
     *
     * @param id the unique identifier of the NGO
     * @return the NgoDto representing the NGO details
     */
    NgoDto getById(UUID id);
}
