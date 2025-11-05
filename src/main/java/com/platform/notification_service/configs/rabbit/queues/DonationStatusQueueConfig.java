package com.platform.notification_service.configs.rabbit.queues;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Donation Status Queue.
 * Extends the QueueAbstractConfig to set up the exchange and queue names.
 */
@Configuration
public class DonationStatusQueueConfig extends QueueAbstractConfig {
    /**
     * Constructs a QueueAbstractConfig with the specified exchange and queue names.
     *
     * @param exchangeNameParam the name of the exchange
     * @param queueNameParam    the name of the queue
     */
    public DonationStatusQueueConfig(@Value("${queues.donation.exchange}") String exchangeNameParam,
                                     @Value("${queues.donation.queue}") String queueNameParam) {
        super(exchangeNameParam, queueNameParam);
    }
}
