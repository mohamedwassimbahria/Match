package com.example.micromatch.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateScoreManuallyRequest {
    @Min(value = 0, message = "Score cannot be negative")
    private int scoreTeam1;
    @Min(value = 0, message = "Score cannot be negative")
    private int scoreTeam2;
}
