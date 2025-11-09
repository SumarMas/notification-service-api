package com.platform.notification_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Main application class for the Notification Service.
 */
@SpringBootApplication
@EnableRabbit
@Slf4j
public class NotificationServiceApplication implements CommandLineRunner {
    /** RabbitListenerEndpointRegistry to manage RabbitMQ listeners. */
    private final RabbitListenerEndpointRegistry registry;

    /**
     * Constructor to initialize RabbitListenerEndpointRegistry.
     * @param registryParam RabbitListenerEndpointRegistry instance
     */
    public NotificationServiceApplication(RabbitListenerEndpointRegistry registryParam) {
        this.registry = registryParam;
    }

    /** Main method to run the Notification Service application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    /**
     * Method to start RabbitMQ listeners on application startup.
     * @param args command-line arguments
     * @throws Exception if an error occurs while starting listeners
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting RabbitMQ Notification Service Application");
        registry.start();
    }
}
