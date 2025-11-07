package com.example.micromatch.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendNotification(String message) {
        // Simulate sending a notification by logging the message
        logger.info("NOTIFICATION: {}", message);
    }
}
