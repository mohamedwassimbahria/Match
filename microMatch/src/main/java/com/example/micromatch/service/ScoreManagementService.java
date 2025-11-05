package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScoreManagementService {

    private final MatchRepository matchRepository;
    private final NotificationService notificationService;

    public Match updateScore(String matchId, String teamId, int points) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        Map<String, Integer> score = match.getScore();
        score.put(teamId, score.getOrDefault(teamId, 0) + points);
        notificationService.sendNotification("Score updated for match " + matchId);
        return matchRepository.save(match);
    }

    public Map<String, Integer> getScore(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        return match.getScore();
    }
}
