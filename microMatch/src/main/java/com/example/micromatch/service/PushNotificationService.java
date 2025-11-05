package com.example.micromatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String destination, String message) {
        // This will send a message to a WebSocket destination, e.g., "/topic/notifications"
        messagingTemplate.convertAndSend(destination, message);
    }
}
