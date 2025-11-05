package com.example.micromatch.service;

import com.example.micromatch.dto.MatchStatistics;
import com.example.micromatch.entity.Match;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchPredictionService {

    private final MatchRepository matchRepository;
    private final MatchAnalyticsService matchAnalyticsService;

    public Map<String, Double> predictOutcome(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));

        MatchStatistics stats = matchAnalyticsService.getMatchStatistics(matchId);

        double team1Strength = calculateTeamStrength(stats, match.getTeam1Id());
        double team2Strength = calculateTeamStrength(stats, match.getTeam2Id());

        double totalStrength = team1Strength + team2Strength;

        Map<String, Double> prediction = new HashMap<>();
        if (totalStrength > 0) {
            prediction.put(match.getTeam1Id(), team1Strength / totalStrength);
            prediction.put(match.getTeam2Id(), team2Strength / totalStrength);
        } else {
            prediction.put(match.getTeam1Id(), 0.5);
            prediction.put(match.getTeam2Id(), 0.5);
        }
        return prediction;
    }

    private double calculateTeamStrength(MatchStatistics stats, String teamId) {
        double passAccuracy = stats.getPassAccuracy().getOrDefault(teamId, 0.0);
        double shotsOnGoal = stats.getShotsOnGoal().getOrDefault(teamId, 0);
        return (passAccuracy * 0.6) + (shotsOnGoal * 0.4);
    }
}
