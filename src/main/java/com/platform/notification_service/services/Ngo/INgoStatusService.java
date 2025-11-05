package com.platform.notification_service.services.Ngo;

import com.platform.notification_service.dtos.Ngo.NgoStatusMessageDto;
/**
 * Service interface for handling NGO status notifications.
 */
public interface INgoStatusService {
    /** Sends a notification about the change in NGO status.
     *
     * @param ngoStatusMessageDto The DTO containing NGO status change information.
     */
    void sendNgoStatusChangeNotification(NgoStatusMessageDto ngoStatusMessageDto);
}
