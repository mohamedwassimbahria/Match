package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignCaptainRequest {
    @NotBlank(message = "Team ID cannot be blank")
    private String teamId;
    @NotBlank(message = "Player ID cannot be blank")
    private String playerId;
}
