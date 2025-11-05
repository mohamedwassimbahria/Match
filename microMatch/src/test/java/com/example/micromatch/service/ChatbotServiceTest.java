package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.entity.Planification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ChatbotServiceTest {

    @InjectMocks
    private ChatbotService chatbotService;

    @Mock
    private MatchService matchService;

    @Mock
    private PlanificationService planificationService;

    @Test
    void testGetNextMatchSchedule() {
        Match match = new Match();
        match.setId("1");
        match.setTeam1Id("Team A");
        match.setTeam2Id("Team B");
        when(matchService.getUpcomingMatches()).thenReturn(Collections.singletonList(match));

        Planification planification = new Planification();
        planification.setDatePropose(LocalDateTime.of(2025, 1, 1, 18, 0));
        when(planificationService.getPlanificationHistory("1")).thenReturn(Collections.singletonList(planification));

        String response = chatbotService.getResponse("What is the schedule for the next match?");
        assertEquals("The next match is Team A vs Team B on 2025-01-01T18:00", response);
    }

    @Test
    void testGetMatchResult() {
        java.util.Map<String, Integer> score = new java.util.HashMap<>();
        score.put("Team A", 2);
        score.put("Team B", 1);
        when(matchService.getFinalScore("1")).thenReturn(score);

        String response = chatbotService.getResponse("What was the score for match 1?");
        assertEquals("The score for match 1 was 2 - 1.", response);
    }

    @Test
    void testGetMatchStatistics() {
        String response = chatbotService.getResponse("Can I get the statistics for match 123?");
        assertEquals("Statistics for match 123 are not yet available.", response);
    }

    @Test
    void testUnknownQuery() {
        String response = chatbotService.getResponse("Tell me a joke.");
        assertEquals("I'm sorry, I can only provide information about match schedules, scores, and statistics.", response);
    }
}
