package com.example.micromatch.dto;

import com.example.micromatch.entity.Match;
import lombok.Data;

@Data
public class AddArbitralDecisionRequest {
    private Match.ArbitralDecision decision;
}
