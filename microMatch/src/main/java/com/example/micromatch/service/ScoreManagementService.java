package com.example.micromatch.service;

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
public class ScoreManagementService {

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
}
