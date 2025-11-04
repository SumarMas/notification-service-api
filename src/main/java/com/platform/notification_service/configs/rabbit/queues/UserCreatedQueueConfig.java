package com.platform.notification_service.configs.rabbit.queues;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCreatedQueueConfig extends QueueAbstractConfig{
    /**
     * Constructs a QueueAbstractConfig with the specified exchange and queue names.
     *
     * @param exchangeNameParam the name of the exchange
     * @param queueNameParam    the name of the queue
     */
    public UserCreatedQueueConfig(@Value("${queues.user-created.exchange}") String exchangeNameParam,
                                  @Value("${queues.user-created.queue}") String queueNameParam) {
        super(exchangeNameParam, queueNameParam);
    }
}
