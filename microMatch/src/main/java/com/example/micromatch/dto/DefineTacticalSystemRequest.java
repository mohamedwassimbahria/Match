package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DefineTacticalSystemRequest {
    @NotBlank(message = "Team ID cannot be blank")
    private String teamId;
    @NotBlank(message = "Tactical system cannot be blank")
    private String tacticalSystem;
}
