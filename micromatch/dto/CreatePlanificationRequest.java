package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreatePlanificationRequest {
    @NotBlank(message = "Match ID cannot be blank")
    private String matchId;
    @NotNull(message = "Proposed date cannot be null")
    private LocalDateTime datePropose;
}
