package com.platform.notification_service.services.Ngo.impl;

import com.platform.notification_service.controllers.manageExceptions.CustomException;
import com.platform.notification_service.dtos.Ngo.NgoDto;
import com.platform.notification_service.restClients.ngos.INgoRestClient;
import com.platform.notification_service.services.Ngo.INgoGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NgoGetService implements INgoGetService {
    /** Service for handling NGO-related REST client operations. */
    private final INgoRestClient ngoRestClient;
    /**
     * Retrieve NGO details by its unique identifier.
     *
     * @param id the unique identifier of the NGO
     * @return the NgoDto representing the NGO details
     */
    @Override
    public NgoDto getById(UUID id) {
        try {
            log.trace("Fetching NGO details for ID: {}", id);
            NgoDto ngoDto = ngoRestClient.getNgoById(id).getBody();
            if (Objects.isNull(ngoDto)) {
                log.error("No NGO found for ID: {}", id);
                throw new CustomException("NGO not found for ID: " + id, HttpStatus.NOT_FOUND);
            }
            log.trace("Successfully fetched NGO details for ID: {}", id);
            return ngoDto;
        } catch (Exception e) {
            log.error("Error fetching NGO details for ID: {}", id, e);
            throw e;
        }
    }
}
