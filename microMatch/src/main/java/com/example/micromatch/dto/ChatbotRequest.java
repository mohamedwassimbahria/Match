package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatbotRequest {
    @NotBlank(message = "Query cannot be blank")
    private String query;
}
