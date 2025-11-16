package com.platform.notification_service.restClients.users;

import com.platform.notification_service.dtos.user.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
/**
 * REST client interface for interacting with the User service.
 */
public interface IUserRestClient {
    /**
     * Retrieves user information by user ID.
     *
     * @param userId the UUID of the user to retrieve
     * @return a ResponseEntity containing the UserDto if found
     */
    ResponseEntity<UserDto> getUserById(UUID userId);
    /**
     * Retrieves a list of admin users.
     *
     * @return a ResponseEntity containing an array of UserDto representing admin users
     */
    ResponseEntity<UserDto[]> getAdminUsers();
}
