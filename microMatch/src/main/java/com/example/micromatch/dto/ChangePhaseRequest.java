package com.example.micromatch.dto;

import com.example.micromatch.enums.MatchPhase;
import lombok.Data;

@Data
public class ChangePhaseRequest {
    private MatchPhase newPhase;
}
