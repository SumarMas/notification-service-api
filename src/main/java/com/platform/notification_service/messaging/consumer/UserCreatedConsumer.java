package com.platform.notification_service.messaging.consumer;

import com.platform.notification_service.dtos.user.UserDto;
import com.platform.notification_service.services.user.IUserCreatedService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedConsumer {
    /** Service for handling user created events. */
    private final IUserCreatedService userCreatedService;

    /**
     * Handles incoming user created messages from the RabbitMQ queue.
     *
     * @param userDto     The user DTO.
     * @param deliveryTag The delivery tag for manual acknowledgment.
     * @param channel     The RabbitMQ channel.
     */
    @RabbitListener(queues = "${queues.user-created.queue}", ackMode = "MANUAL")
    public void handleUserCreatedMessage(@Payload UserDto userDto,
                                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                                         @Header(AmqpHeaders.CHANNEL) Channel channel) {
        try {
            try {
                log.info("Received User Created Message from User Service for user: {}", userDto.getEmail());
                userCreatedService.sendWelcomeMessage(userDto);
                channel.basicAck(deliveryTag, false);
            } catch (Exception ex) {
                log.error("Error processing user created message: {}", ex.getMessage(), ex);
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            log.error("IO exception during message acknowledgment: {}", e.getMessage(), e);
        }

    }
}
