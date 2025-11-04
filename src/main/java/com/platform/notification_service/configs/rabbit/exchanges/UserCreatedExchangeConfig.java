package com.platform.notification_service.configs.rabbit.exchanges;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
/**
 * Configuration class for the "user created" RabbitMQ exchange.
 */
@Configuration
public class UserCreatedExchangeConfig extends ExchangeAbstractConfig {

    /**
     * Constructs a UserCreatedExchangeConfig with the specified exchange name.
     *
     * @param exchangeNameParam the name of the created user exchange,
     *                          injected from application properties
     */
    public UserCreatedExchangeConfig(
            @Value("${queues.user-created.exchange}") String exchangeNameParam) {
        super(exchangeNameParam);
    }

}
