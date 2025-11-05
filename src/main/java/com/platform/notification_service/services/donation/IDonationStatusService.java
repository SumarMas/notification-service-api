package com.platform.notification_service.services.donation;

import com.platform.notification_service.dtos.donation.DonationMessageDto;
/**
 * Service interface for handling donation status notifications.
 */
public interface IDonationStatusService {
    /**
     * Sends a notification regarding the status of a donation.
     *
     * @param donationMessageDto The donation message DTO containing donation details.
     */
    void sendDonationStatusNotification(DonationMessageDto donationMessageDto);
}
