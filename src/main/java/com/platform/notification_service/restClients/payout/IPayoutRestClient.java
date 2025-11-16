package com.platform.notification_service.restClients.payout;

import com.platform.notification_service.dtos.payout.PayoutRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;
/**
 * REST client interface for interacting with the Payout service.
 */
public interface IPayoutRestClient {
    /**
     * Retrieves a PayoutRequestDto by its unique identifier.
     *
     * @param payoutId The unique identifier of the payout request.
     * @return A ResponseEntity containing the PayoutRequestDto
     * corresponding to the provided ID.
     */
    ResponseEntity<PayoutRequestDto> getPayoutById(UUID payoutId);
}
