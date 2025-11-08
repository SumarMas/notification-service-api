package com.platform.notification_service.configs.rabbit.exchanges;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the "campaign close" RabbitMQ exchange.
 */
@Configuration
public class CampaignCloseExchangeConfig extends ExchangeAbstractConfig {
    /**
     * Constructs an ExchangeAbstractConfig with the specified exchange name.
     *
     * @param exchangeNameParam the name of the exchange
     */
    public CampaignCloseExchangeConfig(@Value("${queues.campaign-close.exchange}") String exchangeNameParam) {
        super(exchangeNameParam);
    }
}
