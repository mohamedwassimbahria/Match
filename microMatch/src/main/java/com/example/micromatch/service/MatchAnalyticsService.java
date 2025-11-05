package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchAnalyticsService {

    private final MatchRepository matchRepository;

    public Map<String, Double> calculatePossession(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));

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
}
