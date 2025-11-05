package com.platform.notification_service.messaging.consumer;

import com.platform.notification_service.controllers.manageExceptions.CustomException;
import com.platform.notification_service.dtos.donation.DonationMessageDto;
import com.platform.notification_service.services.donation.IDonationStatusService;
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
 * Consumer class for handling donation messages from RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DonationConsumer {
    /** Service for sending donation status notifications. */
    private final IDonationStatusService donationStatusService;

    /**
     * Handles incoming donation messages from the RabbitMQ queue.
     *
     * @param donationMessageDto The donation message DTO.
     * @param deliveryTag        The delivery tag for manual acknowledgment.
     * @param channel            The RabbitMQ channel.
     */
    @RabbitListener(queues = "${queues.donation.queue}", ackMode = "MANUAL")
    public void handleDonationMessage(@Payload DonationMessageDto donationMessageDto,
                                      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                                      @Header(AmqpHeaders.CHANNEL) Channel channel) {
        try {
            try {
                log.info("Received Donation Message from Donation Service");
                donationStatusService.sendDonationStatusNotification(donationMessageDto);
                channel.basicAck(deliveryTag, false);
            } catch (CustomException ex) {
                log.error("Custom exception processing donation message: {}", ex.getMessage());
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception ex) {
                log.error("Unexpected error processing donation message: {}", ex.getMessage());
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            log.error("IO exception during message acknowledgment: {}", e.getMessage());
        }
    }
}
