package com.example.micromatch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ImprovedChatbotService {

    private final MatchService matchService;
    private static final Pattern MATCH_ID_PATTERN = Pattern.compile("\\b[a-fA-F0-9]{24}\\b");

    public String getResponse(String query) {
        String lowerCaseQuery = query.toLowerCase();

        if (lowerCaseQuery.contains("score")) {
            String matchId = extractMatchId(lowerCaseQuery);
            if (matchId != null) {
                return matchService.getFinalScore(matchId).toString();
            } else {
                return "Please provide a valid match ID to get the score.";
            }
        } else if (lowerCaseQuery.contains("schedule") || lowerCaseQuery.contains("next match")) {
            return matchService.getUpcomingMatches(Pageable.unpaged()).getContent().get(0).toString();
        } else {
            return "I can't answer that. Ask me about scores or schedules.";
        }
    }

    private String extractMatchId(String query) {
        Matcher matcher = MATCH_ID_PATTERN.matcher(query);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
