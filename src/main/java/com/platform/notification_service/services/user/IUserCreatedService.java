package com.platform.notification_service.services.user;

import com.platform.notification_service.dtos.user.UserDto;

/**
 * Service interface for handling actions related to user creation events.
 */
public interface IUserCreatedService {
    /**
     * Sends a welcome message to the newly created user.
     *
     * @param userDto The DTO containing user information.
     */
    void sendWelcomeMessage(UserDto userDto);
}
