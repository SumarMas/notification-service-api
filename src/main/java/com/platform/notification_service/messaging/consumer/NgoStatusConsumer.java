package com.platform.notification_service.messaging.consumer;

import com.platform.notification_service.dtos.Ngo.NgoStatusMessageDto;
import com.platform.notification_service.services.Ngo.INgoStatusService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * Consumer for handling NGO status change messages from the message queue.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NgoStatusConsumer {
    /** Service for handling NGO status changes. */
    private final INgoStatusService ngoStatusService;
    /**
     * Handles incoming NGO status change messages from the RabbitMQ queue.
     *
     * @param ngoStatusMessageDto The NGO status message DTO.
     * @param deliveryTag         The delivery tag for manual acknowledgment.
     * @param channel             The RabbitMQ channel.
     */
    @RabbitListener(queues = "${queues.ngo-status.queue}", ackMode = "MANUAL")
    public void handleNgoStatusChangeMessage(@Payload NgoStatusMessageDto ngoStatusMessageDto,
                                             @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                                             @Header(AmqpHeaders.CHANNEL) Channel channel) {
        try {
            try {
                log.info("Received NGO Status Change Message for NGO: {}", ngoStatusMessageDto.getNameNgo());
                ngoStatusService.sendNgoStatusChangeNotification(ngoStatusMessageDto);
                channel.basicAck(deliveryTag, false);
            } catch (Exception ex) {
                log.error("Error processing NGO status change message: {}", ex.getMessage(), ex);
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            log.error("Error processing NGO status change message: {}", e.getMessage(), e);
        }
    }
}
