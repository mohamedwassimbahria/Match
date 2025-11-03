package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.enums.MatchStatus;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public Match createMatch(String team1Id, String team2Id, LocalDateTime date) {
        Match match = Match.builder()
                .team1Id(team1Id)
                .team2Id(team2Id)
                .date(date)
                .status(MatchStatus.SCHEDULED.name())
                .score(new HashMap<>())
                .events(new ArrayList<>())
                .build();
        return matchRepository.save(match);
    }

    public Match startMatch(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found"));
        match.setStatus(MatchStatus.LIVE.name());
        return matchRepository.save(match);
    }

    public Match finishMatch(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found"));
        match.setStatus(MatchStatus.FINISHED.name());
        return matchRepository.save(match);
    }

    public Match updateScore(String matchId, String teamId, int score) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found"));
        match.getScore().put(teamId, score);
        return matchRepository.save(match);
    }

    public Match addEvent(String matchId, String event) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found"));
        match.getEvents().add(event);
        return matchRepository.save(match);
    }
}
