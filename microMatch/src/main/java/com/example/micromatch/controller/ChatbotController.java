package com.example.micromatch.controller;

import com.example.micromatch.dto.ChatbotRequest;
import com.example.micromatch.dto.ChatbotResponse;
import com.example.micromatch.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/query")
    public ChatbotResponse getChatbotResponse(@RequestBody ChatbotRequest request) {
        String response = chatbotService.getResponse(request.getQuery());
        return new ChatbotResponse(response);
    }
}
