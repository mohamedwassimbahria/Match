package com.example.micromatch.dto;

import com.example.micromatch.enums.MatchPhase;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePhaseRequest {
    @NotNull(message = "New phase cannot be null")
    private MatchPhase newPhase;
}
