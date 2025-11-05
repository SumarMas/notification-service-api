package com.platform.notification_service.configs.rabbit.exchanges;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the "NGO status" RabbitMQ exchange.
 */
@Configuration
public class NgoStatusExchangeConfig extends ExchangeAbstractConfig {
    /**
     * Constructs a NgoStatusExchangeConfig with the specified exchange name.
     * @param exchangeNameParam the name of the NGO status exchange,
     *                          injected from application properties
     */
    public NgoStatusExchangeConfig(@Value("${queues.ngo-status.exchange}") String exchangeNameParam) {
        super(exchangeNameParam);
    }
}
