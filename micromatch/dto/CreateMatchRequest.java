package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateMatchRequest {
    @NotBlank(message = "Team1 ID cannot be blank")
    private String team1Id;
    @NotBlank(message = "Team2 ID cannot be blank")
    private String team2Id;
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;
}
