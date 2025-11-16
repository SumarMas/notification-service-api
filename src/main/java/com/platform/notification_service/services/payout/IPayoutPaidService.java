package com.platform.notification_service.services.payout;

import com.platform.notification_service.dtos.payout.PayoutMessageDto;
/**
 * Service interface for handling payout paid notifications.
 */
public interface IPayoutPaidService {
    /** Sends a notification when a payout has been paid.
     *
     * @param payoutMessageDto the payout message data transfer object
     */
    void sendPayoutPaidNotification(PayoutMessageDto payoutMessageDto);
}
