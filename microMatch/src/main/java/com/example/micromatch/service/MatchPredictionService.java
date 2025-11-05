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
public class MatchPredictionService {

    private final MatchRepository matchRepository;

    public Map<String, Double> predictOutcome(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));

        Map<String, Integer> score = match.getScore();
        int score1 = score.get(match.getTeam1Id());
        int score2 = score.get(match.getTeam2Id());

        Map<String, Double> prediction = new HashMap<>();
        if (score1 > score2) {
            prediction.put(match.getTeam1Id(), 0.8);
            prediction.put(match.getTeam2Id(), 0.2);
        } else if (score2 > score1) {
            prediction.put(match.getTeam1Id(), 0.2);
            prediction.put(match.getTeam2Id(), 0.8);
        } else {
            prediction.put(match.getTeam1Id(), 0.5);
            prediction.put(match.getTeam2Id(), 0.5);
        }
        return prediction;
    }
}
