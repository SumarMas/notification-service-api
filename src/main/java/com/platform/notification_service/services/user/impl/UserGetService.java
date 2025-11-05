package com.platform.notification_service.services.user.impl;

import com.platform.notification_service.controllers.manageExceptions.CustomException;
import com.platform.notification_service.dtos.user.UserDto;
import com.platform.notification_service.restClients.users.IUserRestClient;
import com.platform.notification_service.services.user.IUserGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserGetService implements IUserGetService {
    /** REST client for user-related operations. */
    private final IUserRestClient userRestClient;
    /**
     * Retrieve user details by its unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the UserDto representing the user details
     */
    @Override
    public UserDto getUserById(UUID id) {
        log.trace("Fetching User details for ID: {}", id);
        try {
            UserDto userDto = userRestClient.getUserById(id).getBody();
            if (userDto == null) {
                log.error("User not found for ID: {}", id);
                throw new CustomException("User not found", HttpStatus.NOT_FOUND);
            }
            return userDto;

        } catch (Exception e) {
            log.error("Error retrieving User details for ID: {}", id, e);
            throw e;
        }
    }
}
