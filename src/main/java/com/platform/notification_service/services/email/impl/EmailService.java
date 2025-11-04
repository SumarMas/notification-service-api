package com.platform.notification_service.services.email.impl;

import com.platform.notification_service.controllers.manageExceptions.CustomException;
import com.platform.notification_service.services.email.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailService {
    /** JavaMailSender for sending emails. */
    private final JavaMailSender mailSender;
    /**
     * Sends an HTML email.
     *
     * @param to      recipient email
     * @param cc      optional CC email (nullable)
     * @param subject email subject
     * @param content HTML content to send
     */
    @Override
    public void send(String to, String cc, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(to);
            if (cc != null && !cc.isBlank()) {
                helper.setCc(cc);
            }
            helper.setSubject(subject);
            log.info(content);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException("Error sending email to " + to, HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
