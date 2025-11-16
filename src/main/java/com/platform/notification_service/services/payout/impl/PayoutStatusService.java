package com.platform.notification_service.services.payout.impl;

import com.platform.notification_service.dtos.payout.PayoutMessageDto;
import com.platform.notification_service.enums.PayoutStatus;
import com.platform.notification_service.services.payout.IPayoutPaidService;
import com.platform.notification_service.services.payout.IPayoutPendingService;
import com.platform.notification_service.services.payout.IPayoutStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IPayoutStatusService interface
 * for handling payout status updates.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayoutStatusService implements IPayoutStatusService {
    /**
     * Service for handling payout paid events.
     */
    private final IPayoutPaidService payoutPaidService;
    /**
     * Service for handling payout pending events.
     */
    private final IPayoutPendingService payoutPendingService;

    /**
     * Handles the update of payout status
     * based on the provided payout request data.
     *
     * @param messageDto the payout message
     *                   data transfer object.
     */
    @Override
    public void handlePayoutStatusUpdate(PayoutMessageDto messageDto) {
        log.trace("handlePayoutStatusUpdate payoutRequestDto={}", messageDto);
        PayoutStatus previousStatus = messageDto.getPreviousPayoutStatus();
        PayoutStatus currentStatus = messageDto.getPayoutStatus();
        if ((previousStatus == null || previousStatus.equals(PayoutStatus.PENDING))
                && currentStatus.equals(PayoutStatus.PENDING)) {
            log.trace("Managing donation paid event for payout status update: {}", messageDto);
            payoutPendingService.sendPayoutPendingNotification(messageDto);
        } else if (previousStatus == null) {
            log.warn("Received PayoutStatus update with null previous status: {}", messageDto);
        } else if (previousStatus.equals(PayoutStatus.PENDING)
                && currentStatus.equals(PayoutStatus.APPROVED)) {
            log.trace("Managing payout paid event for payout status update: {}", messageDto);
            payoutPaidService.sendPayoutPaidNotification(messageDto);
        } else {
            log.debug("No action taken for payout status update: {}", messageDto);
        }
    }
}
