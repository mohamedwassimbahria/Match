package com.example.micromatch.service;

import com.example.micromatch.entity.Match;
import com.example.micromatch.enums.EventType;
import com.example.micromatch.enums.MatchPhase;
import com.example.micromatch.enums.MatchStatus;
import com.example.micromatch.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MatchService matchService;

    @Test
    void createMatch() {
        Match match = new Match();
        match.setId("1");
        match.setTeam1Id("1");
        match.setTeam2Id("2");
        match.setStatus(MatchStatus.SCHEDULED.name());
        match.setMatchPhase(MatchPhase.PRE_MATCH);
        match.setScore(new HashMap<>());
        match.getScore().put("1", 0);
        match.getScore().put("2", 0);
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        Match created = matchService.createMatch("1", "2", LocalDateTime.now());
        assertEquals(MatchStatus.SCHEDULED.name(), created.getStatus());
        assertEquals(MatchPhase.PRE_MATCH, created.getMatchPhase());
        assertEquals(0, created.getScore().get("1"));
        assertEquals(0, created.getScore().get("2"));
        verify(notificationService).sendNotification("Match created between 1 and 2");
    }

    @Test
    void startMatch() {
        Match match = new Match();
        match.setId("1");
        match.setStatus(MatchStatus.SCHEDULED.name());
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        Match started = matchService.startMatch("1");
        assertEquals(MatchStatus.LIVE.name(), started.getStatus());
        assertEquals(MatchPhase.FIRST_HALF, started.getMatchPhase());
        verify(notificationService).sendNotification("Match 1 has started");
    }

    @Test
    void addGoalEvent() {
        Match match = new Match();
        match.setId("1");
        match.setTeam1Id("1");
        match.setTeam2Id("2");
        match.setScore(new HashMap<>());
        match.getScore().put("1", 0);
        match.getScore().put("2", 0);
        match.setEvents(new ArrayList<>());
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match.Event goal = new Match.Event(null, 45, EventType.GOAL.name(), "1", null, "Goal by Player 10");
        Match updated = matchService.addEvent("1", goal);

        assertEquals(1, updated.getScore().get("1"));
        assertEquals(1, updated.getEvents().size());
        verify(notificationService).sendNotification("Goal scored in match 1 by team 1");
    }

    @Test
    void defineLineup() {
        Match match = new Match();
        match.setId("1");
        match.setTeam1Id("1");
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        List<String> lineup = List.of("p1", "p2", "p3");
        Match updated = matchService.defineLineup("1", "1", lineup);

        assertEquals(lineup, updated.getTeam1Lineup());
    }

    @Test
    void recordCollectiveStats() {
        Match match = new Match();
        match.setId("1");
        match.setTeam1Id("1");
        match.setCollectiveStats(new HashMap<>());
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Map<String, Object> stats = new HashMap<>();
        stats.put("possession", 60);
        Match updated = matchService.recordCollectiveStats("1", "1", stats);

        assertEquals(stats, updated.getCollectiveStats().get("1"));
    }

    @Test
    void addMedia() {
        Match match = new Match();
        match.setId("1");
        match.setMedia(new ArrayList<>());
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match.Media media = new Match.Media(null, "PHOTO", "http://example.com/photo.jpg", "Match Photo");
        Match updated = matchService.addMedia("1", media);

        assertEquals(1, updated.getMedia().size());
    }

    @Test
    void assignMainReferee() {
        Match match = new Match();
        match.setId("1");
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match updated = matchService.assignMainReferee("1", "John Doe");

        assertEquals("John Doe", updated.getMainReferee());
    }

    @Test
    void assignAssistantReferees() {
        Match match = new Match();
        match.setId("1");
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        List<String> assistants = List.of("Assistant 1", "Assistant 2");
        Match updated = matchService.assignAssistantReferees("1", assistants);

        assertEquals(assistants, updated.getAssistantReferees());
    }

    @Test
    void assignFourthOfficial() {
        Match match = new Match();
        match.setId("1");
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match updated = matchService.assignFourthOfficial("1", "Fourth Official");

        assertEquals("Fourth Official", updated.getFourthOfficial());
    }

    @Test
    void assignVarReferees() {
        Match match = new Match();
        match.setId("1");
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        List<String> var = List.of("VAR 1", "VAR 2");
        Match updated = matchService.assignVarReferees("1", var);

        assertEquals(var, updated.getVarReferees());
    }

    @Test
    void addAdditionalTime() {
        Match match = new Match();
        match.setId("1");
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match updated = matchService.addAdditionalTime("1", 5);

        assertEquals(5, updated.getAdditionalTime());
    }

    @Test
    void getScoreHistory() {
        Match match = new Match();
        match.setId("1");
        match.setTeam1Id("1");
        match.setTeam2Id("2");
        match.setScore(new HashMap<>());
        match.getScore().put("1", 0);
        match.getScore().put("2", 0);
        match.setEvents(new ArrayList<>());
        Match.Event goal1 = new Match.Event(null, 30, EventType.GOAL.name(), "1", null, "Goal");
        Match.Event goal2 = new Match.Event(null, 60, EventType.GOAL.name(), "2", null, "Goal");
        match.getEvents().add(goal1);
        match.getEvents().add(goal2);

        when(matchRepository.findById("1")).thenReturn(Optional.of(match));

        Map<Integer, Map<String, Integer>> scoreHistory = matchService.getScoreHistory("1");

        assertEquals(3, scoreHistory.size());
        assertEquals(0, scoreHistory.get(0).get("1"));
        assertEquals(1, scoreHistory.get(30).get("1"));
        assertEquals(1, scoreHistory.get(60).get("2"));
    }

    @Test
    void addArbitralDecision() {
        Match match = new Match();
        match.setId("1");
        match.setDecisions(new ArrayList<>());
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match.ArbitralDecision decision = new Match.ArbitralDecision(null, 75, "YELLOW_CARD", "Foul");
        Match updated = matchService.addArbitralDecision("1", decision);

        assertEquals(1, updated.getDecisions().size());
    }

    @Test
    void calculateTotalMatchDuration() {
        Match match = new Match();
        match.setId("1");
        match.setStartTime(LocalDateTime.now());
        match.setEndTime(LocalDateTime.now().plusMinutes(90));
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));

        long duration = matchService.calculateTotalMatchDuration("1");

        assertEquals(90, duration);
    }

    @Test
    void getMatchTimeline() {
        Match match = new Match();
        match.setId("1");
        match.setEvents(new ArrayList<>());
        Match.Event goal1 = new Match.Event(null, 60, EventType.GOAL.name(), "1", null, "Goal");
        Match.Event goal2 = new Match.Event(null, 30, EventType.GOAL.name(), "2", null, "Goal");
        match.getEvents().add(goal1);
        match.getEvents().add(goal2);
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));

        List<Match.Event> timeline = matchService.getMatchTimeline("1");

        assertEquals(2, timeline.size());
        assertEquals(30, timeline.get(0).getMinute());
        assertEquals(60, timeline.get(1).getMinute());
    }

    @Test
    void updateCurrentMinute() {
        Match match = new Match();
        match.setId("1");
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match updated = matchService.updateCurrentMinute("1", 75);

        assertEquals(75, updated.getCurrentMinute());
    }

    @Test
    void addRedCardEvent() {
        Match match = new Match();
        match.setId("1");
        match.setEvents(new ArrayList<>());
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match.Event redCard = new Match.Event(null, 80, EventType.RED_CARD.name(), "1", null, "Red Card");
        matchService.addEvent("1", redCard);

        verify(notificationService).sendNotification("Red card in match 1");
    }

    @Test
    void changePhaseToHalfTime() {
        Match match = new Match();
        match.setId("1");
        when(matchRepository.findById("1")).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        matchService.changePhase("1", MatchPhase.HALF_TIME);

        verify(notificationService).sendNotification("Half-time in match 1");
    }
}
