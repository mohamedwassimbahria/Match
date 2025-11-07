package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.entity.Planification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final MatchService matchService;
    private final PlanificationService planificationService;

    public String getResponse(String query) {
        String lowerCaseQuery = query.toLowerCase();

        if (lowerCaseQuery.contains("schedule") || lowerCaseQuery.contains("next match")) {
            return getNextMatchSchedule();
        } else if (lowerCaseQuery.contains("score") || lowerCaseQuery.contains("result")) {
            return getMatchResult(lowerCaseQuery);
        } else if (lowerCaseQuery.contains("stats") || lowerCaseQuery.contains("statistics")) {
            return getMatchStatistics(lowerCaseQuery);
        } else {
            return "I'm sorry, I can only provide information about match schedules, scores, and statistics.";
        }
    }

    private String getNextMatchSchedule() {
        // This is a simplified implementation. A real implementation would need a way to identify the "next" match.
        Page<Match> matches = matchService.getUpcomingMatches(Pageable.unpaged());
        if (matches.isEmpty()) {
            return "There are no upcoming matches scheduled.";
        }
        Match nextMatch = matches.getContent().get(0);
        return "The next match is " + nextMatch.getTeam1Id() + " vs " + nextMatch.getTeam2Id() + " on " + nextMatch.getDate();
    }

    private String getMatchResult(String query) {
        // This requires a way to identify the match the user is asking about.
        // For simplicity, we'll assume the user provides the match ID.
        try {
            String matchId = extractMatchId(query);
            if (matchId != null) {
                Map<String, Integer> score = matchService.getFinalScore(matchId);
                String team1Id = score.keySet().toArray()[0].toString();
                String team2Id = score.keySet().toArray()[1].toString();
                return "The score for match " + matchId + " was " + score.get(team1Id) + " - " + score.get(team2Id) + ".";
            } else {
                return "Please provide a valid match ID to get the score.";
            }
        } catch (Exception e) {
            return "I couldn't find a match with that ID.";
        }
    }

    private String getMatchStatistics(String query) {
        // Similar to getMatchResult, this assumes a match ID is provided.
        try {
            String matchId = extractMatchId(query);
            if (matchId == null) {
                return "Please provide a match ID to get statistics.";
            }
            // The current Match entity doesn't have detailed statistics. This is a placeholder.
            return "Statistics for match " + matchId + " are not yet available.";
        } catch (Exception e) {
            return "I couldn't find a match with that ID.";
        }
    }

    private String extractMatchId(String query) {
        // A simple way to extract a potential match ID.
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b[a-fA-F0-9]{24}\\b");
        java.util.regex.Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
