package com.platform.notification_service.messaging.consumer;

import com.platform.notification_service.controllers.manageExceptions.CustomException;
import com.platform.notification_service.dtos.campaign.CampaignClosedEventDto;
import com.platform.notification_service.services.campaigns.ICampaignCloseService;
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
 * Consumer class for handling campaign close messages from RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignCloseConsumer {
    /** Service for sending campaign close notifications. */
    private final ICampaignCloseService campaignCloseService;

    /**
     * Handles incoming campaign close messages from the RabbitMQ queue.
     *
     * @param campaignClosedEventDto The campaign closed event DTO.
     * @param deliveryTag            The delivery tag for manual acknowledgment.
     * @param channel                The RabbitMQ channel.
     */
    @RabbitListener(queues = "${queues.campaign-close.queue}", ackMode = "MANUAL")
    public void handleCampaignCloseMessage(@Payload CampaignClosedEventDto campaignClosedEventDto,
                                      @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                                      @Header(AmqpHeaders.CHANNEL) Channel channel) {
        try {
            try {
                log.info("Received Campaign Close Message from Campaign Service");
                campaignCloseService.sendCampaignCloseNotification(campaignClosedEventDto);
                channel.basicAck(deliveryTag, false);
            } catch (CustomException ex) {
                log.error("Custom exception processing Campaign message: {}", ex.getMessage());
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception ex) {
                log.error("Unexpected error processing Campaign message: {}", ex.getMessage());
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (IOException e) {
            log.error("IO exception during message acknowledgment: {}", e.getMessage());
        }
    }
}
