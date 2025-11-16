package com.platform.notification_service.services.user;

import com.platform.notification_service.dtos.user.UserDto;

import java.util.List;
import java.util.UUID;
/**
 * Service interface for retrieving user details.
 */
public interface IUserGetService {
    /** Retrieve user details by its unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the UserDto representing the user details
     */
    UserDto getUserById(UUID id);
    /** Retrieve a list of all admin users.
     *
     * @return a list of UserDto representing admin users
     */
    List<UserDto> getAdminUsers();
}
