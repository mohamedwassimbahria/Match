package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImprovedChatbotService {

    private final MatchService matchService;

    public String getResponse(String query) {
        String lowerCaseQuery = query.toLowerCase();

        if (lowerCaseQuery.contains("score")) {
            String matchId = extractMatchId(lowerCaseQuery);
            if (matchId != null) {
                return matchService.getFinalScore(matchId).toString();
            } else {
                return "Please provide a match ID to get the score.";
            }
        } else if (lowerCaseQuery.contains("schedule") || lowerCaseQuery.contains("next match")) {
            return matchService.getUpcomingMatches(Pageable.unpaged()).getContent().get(0).toString();
        } else {
            return "I can't answer that. Ask me about scores or schedules.";
        }
    }

    private String extractMatchId(String query) {
        // Simple keyword-based extraction. A real implementation would use NLP.
        String[] words = query.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("match") && i + 1 < words.length) {
                return words[i + 1];
            }
        }
        return null;
    }
}
