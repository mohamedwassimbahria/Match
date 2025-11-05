package com.example.micromatch.service;

import com.example.micromatch.dto.MatchStatistics;
import com.example.micromatch.entity.Match;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchAnalyticsService {

    private final MatchRepository matchRepository;

    public MatchStatistics getMatchStatistics(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));

        return MatchStatistics.builder()
                .possession(calculatePossession(match))
                .shotsOnGoal(calculateShotsOnGoal(match))
                .passAccuracy(calculatePassAccuracy(match))
                .topPerformingPlayer(findTopPerformingPlayer(match))
                .build();
    }

    private Map<String, Double> calculatePossession(Match match) {
        long team1Events = match.getEvents().stream().filter(e -> e.getTeamId().equals(match.getTeam1Id())).count();
        long team2Events = match.getEvents().stream().filter(e -> e.getTeamId().equals(match.getTeam2Id())).count();
        long totalEvents = team1Events + team2Events;

        Map<String, Double> possession = new HashMap<>();
        if (totalEvents > 0) {
            possession.put(match.getTeam1Id(), (double) team1Events / totalEvents);
            possession.put(match.getTeam2Id(), (double) team2Events / totalEvents);
        } else {
            possession.put(match.getTeam1Id(), 0.5);
            possession.put(match.getTeam2Id(), 0.5);
        }
        return possession;
    }

    private Map<String, Integer> calculateShotsOnGoal(Match match) {
        Map<String, Integer> shotsOnGoal = new HashMap<>();
        shotsOnGoal.put(match.getTeam1Id(), 0);
        shotsOnGoal.put(match.getTeam2Id(), 0);

        for (Match.Event event : match.getEvents()) {
            if ("shot_on_goal".equals(event.getType())) {
                shotsOnGoal.put(event.getTeamId(), shotsOnGoal.get(event.getTeamId()) + 1);
            }
        }
        return shotsOnGoal;
    }

    private Map<String, Double> calculatePassAccuracy(Match match) {
        Map<String, Double> passAccuracy = new HashMap<>();
        passAccuracy.put(match.getTeam1Id(), 0.0);
        passAccuracy.put(match.getTeam2Id(), 0.0);

        for (String teamId : passAccuracy.keySet()) {
            long successfulPasses = match.getIndividualStats().stream()
                    .filter(p -> p.getTeamId().equals(teamId))
                    .mapToLong(p -> (long) p.getStats().getOrDefault("successful_passes", 0L))
                    .sum();
            long totalPasses = match.getIndividualStats().stream()
                    .filter(p -> p.getTeamId().equals(teamId))
                    .mapToLong(p -> (long) p.getStats().getOrDefault("total_passes", 0L))
                    .sum();
            if (totalPasses > 0) {
                passAccuracy.put(teamId, (double) successfulPasses / totalPasses);
            }
        }
        return passAccuracy;
    }

    private Map<String, String> findTopPerformingPlayer(Match match) {
        Map<String, String> topPerformers = new HashMap<>();

        for (String teamId : new String[]{match.getTeam1Id(), match.getTeam2Id()}) {
            Optional<Match.PlayerStats> topPlayer = match.getIndividualStats().stream()
                    .filter(p -> p.getTeamId().equals(teamId))
                    .max((p1, p2) -> {
                        double rating1 = (double) p1.getStats().getOrDefault("rating", 0.0);
                        double rating2 = (double) p2.getStats().getOrDefault("rating", 0.0);
                        return Double.compare(rating1, rating2);
                    });
            topPerformers.put(teamId, topPlayer.isPresent() ? topPlayer.get().getPlayerId() : "N/A");
        }
        return topPerformers;
    }
}
