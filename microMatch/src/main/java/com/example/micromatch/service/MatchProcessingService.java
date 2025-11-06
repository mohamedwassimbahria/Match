package com.example.micromatch.service;

import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import com.example.micromatch.entity.Match;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchProcessingService {

    private final MatchRepository matchRepository;
    private final NotificationService notificationService;
    private final MongoTemplate mongoTemplate;

    public Match updateScore(String matchId, String teamId, int points) {
        Query query = new Query(Criteria.where("id").is(matchId));
        Update update = new Update().inc("score." + teamId, points);
        mongoTemplate.updateFirst(query, update, Match.class);

        notificationService.sendNotification("Score updated for match " + matchId);

        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
    }

    public Map<String, Integer> getScore(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        return match.getScore();
    }

    public Match reviewIncident(String matchId, String incidentDescription, int minute) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));

        Match.ArbitralDecision decision = new Match.ArbitralDecision();
        decision.setDecision("VAR Review");
        decision.setDescription(incidentDescription);
        decision.setMinute(minute);

        if (match.getDecisions() == null) {
            match.setDecisions(new java.util.ArrayList<>());
        }
        match.getDecisions().add(decision);

        notificationService.sendNotification("VAR review completed for match " + matchId);
        return matchRepository.save(match);
    }

    public com.example.micromatch.dto.MatchStatistics getMatchStatistics(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));

        return com.example.micromatch.dto.MatchStatistics.builder()
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

        Map<String, Double> possession = new java.util.HashMap<>();
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
        Map<String, Integer> shotsOnGoal = new java.util.HashMap<>();
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
        Map<String, Double> passAccuracy = new java.util.HashMap<>();
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
        Map<String, String> topPerformers = new java.util.HashMap<>();

        for (String teamId : new String[]{match.getTeam1Id(), match.getTeam2Id()}) {
            java.util.Optional<Match.PlayerStats> topPlayer = match.getIndividualStats().stream()
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

    public Map<String, Double> predictOutcome(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));

        com.example.micromatch.dto.MatchStatistics stats = getMatchStatistics(matchId);

        double team1Strength = calculateTeamStrength(stats, match.getTeam1Id());
        double team2Strength = calculateTeamStrength(stats, match.getTeam2Id());

        double totalStrength = team1Strength + team2Strength;

        Map<String, Double> prediction = new java.util.HashMap<>();
        if (totalStrength > 0) {
            prediction.put(match.getTeam1Id(), team1Strength / totalStrength);
            prediction.put(match.getTeam2Id(), team2Strength / totalStrength);
        } else {
            prediction.put(match.getTeam1Id(), 0.5);
            prediction.put(match.getTeam2Id(), 0.5);
        }
        return prediction;
    }

    private double calculateTeamStrength(com.example.micromatch.dto.MatchStatistics stats, String teamId) {
        double passAccuracy = stats.getPassAccuracy().getOrDefault(teamId, 0.0);
        double shotsOnGoal = stats.getShotsOnGoal().getOrDefault(teamId, 0);
        return (passAccuracy * 0.6) + (shotsOnGoal * 0.4);
    }
}
