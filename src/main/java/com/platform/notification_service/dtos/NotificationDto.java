package com.platform.notification_service.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.platform.notification_service.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Data Transfer Object (DTO) representing an in-app notification.
 */
@Data
@Builder
public class NotificationDto {
    /** Unique identifier for the notification. */
    @JsonProperty("notification_id")
    private UUID notificationId;
    /** Identifier of the user receiving the notification. */
    @JsonProperty("user_id")
    private UUID userId;
    /** Title of the notification. */
    @JsonProperty("title")
    private String title;
    /** Message content of the notification. */
    @JsonProperty("message")
    private String message;
    /** Type of the notification. */
    @JsonProperty("type")
    private NotificationType type;
    /** Flag indicating whether the notification has been read. */
    @JsonProperty("read")
    private boolean read;
    /** LocalDateTime when the notification was created. */
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    /** LocalDateTime when the notification was last updated. */
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


}
