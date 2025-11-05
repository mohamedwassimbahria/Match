package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class DefineSubstitutesRequest {
    @NotBlank(message = "Team ID cannot be blank")
    private String teamId;
    @NotEmpty(message = "Player IDs list cannot be empty")
    private List<String> playerIds;
}
