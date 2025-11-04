package com.platform.notification_service.configs.rabbit.queues;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public abstract class QueueAbstractConfig {
    /** Name of the exchange. */
    private final String exchangeName;
    /** Name of the queue. */
    private final String queueName;

    /**
     * Constructs a QueueAbstractConfig with the specified exchange and queue names.
     *
     * @param exchangeNameParam the name of the exchange
     * @param queueNameParam    the name of the queue
     */
    public QueueAbstractConfig(String exchangeNameParam, String queueNameParam) {
        this.exchangeName = exchangeNameParam;
        this.queueName = queueNameParam;
    }

    /**
     * Defines the durable queue bean.
     * A durable queue is one that persists
     * across broker restarts, ensuring message durability.
     *
     * @return A Queue object representing the durable queue.
     */
    @Bean(name = "#{T(java.lang.String).format('%sQueue', queueName)}")
    public Queue queue() {
        return new Queue(queueName, true); // true indicates the queue is durable
    }

    /**
     * Binds the durable queue to the Fanout Exchange.
     * This ensures that any message sent
     * to the Fanout Exchange is broadcast to the bound queue.
     *
     * @param exchange The Fanout Exchange bean.
     * @param queue The durable Donation Queue bean.
     * @return A Binding object representing the
     * connection between the exchange and the queue.
     */
    @Bean(name = "#{T(java.lang.String).format('%sBinding', queueName)}")
    public Binding bindingDonationQueue(
            @Qualifier("#{T(java.lang.String).format('%sExchange', exchangeName)}") FanoutExchange exchange,
            @Qualifier("#{T(java.lang.String).format('%sQueue', queueName)}") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}
