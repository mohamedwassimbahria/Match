package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VARService {

    private final MatchRepository matchRepository;
    private final NotificationService notificationService;

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
}
