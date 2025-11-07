package com.example.micromatch.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class MatchStatistics {
    private Map<String, Double> possession;
    private Map<String, Integer> shotsOnGoal;
    private Map<String, Double> passAccuracy;
    private Map<String, String> topPerformingPlayer;
}
