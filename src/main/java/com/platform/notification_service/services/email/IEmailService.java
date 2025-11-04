package com.platform.notification_service.services.email;
/**
 * Service for sending HTML emails.
 */
public interface IEmailService {
    /**
     * Sends an HTML email.
     *
     * @param to      recipient email
     * @param cc      optional CC email (nullable)
     * @param subject email subject
     * @param content HTML content to send
     */
    void send(String to, String cc, String subject, String content);
}
