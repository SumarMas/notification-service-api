package com.platform.notification_service.services.payout;

import com.platform.notification_service.dtos.payout.PayoutMessageDto;
/**
 * Service interface for handling payout pending notifications.
 */
public interface IPayoutPendingService {
    /**
     * Sends a notification when a payout is pending.
     *
     * @param payoutMessageDto the payout message data transfer object
     */
    void sendPayoutPendingNotification(PayoutMessageDto payoutMessageDto);
}
