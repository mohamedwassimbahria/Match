package com.example.micromatch.dto;

import com.example.micromatch.entity.Match;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddArbitralDecisionRequest {
    @NotNull(message = "Decision cannot be null")
    private Match.ArbitralDecision decision;
}
