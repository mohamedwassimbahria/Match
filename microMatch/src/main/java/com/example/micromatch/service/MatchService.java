package com.example.micromatch.service;

import com.example.micromatch.dto.SearchMatchesRequest;
import com.example.micromatch.entity.Match;
import com.example.micromatch.enums.EventType;
import com.example.micromatch.enums.MatchPhase;
import com.example.micromatch.enums.MatchStatus;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final NotificationService notificationService;
    private final MongoTemplate mongoTemplate;
    private final ScoreManagementService scoreManagementService;

    public Match createMatch(String team1Id, String team2Id, LocalDateTime date) {
        Match match = Match.builder()
                .team1Id(team1Id)
                .team2Id(team2Id)
                .date(date)
                .status(MatchStatus.SCHEDULED.name())
                .matchPhase(MatchPhase.PRE_MATCH)
                .score(new HashMap<>())
                .events(new ArrayList<>())
                .collectiveStats(new HashMap<>())
                .individualStats(new ArrayList<>())
                .media(new ArrayList<>())
                .build();
        match.getScore().put(team1Id, 0);
        match.getScore().put(team2Id, 0);
        notificationService.sendNotification("Match created between " + team1Id + " and " + team2Id);
        return matchRepository.save(match);
    }

    public Match startMatch(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setStatus(MatchStatus.LIVE.name());
        match.setMatchPhase(MatchPhase.FIRST_HALF);
        match.setStartTime(LocalDateTime.now());
        notificationService.sendNotification("Match " + matchId + " has started");
        return matchRepository.save(match);
    }

    public Match finishMatch(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setStatus(MatchStatus.FINISHED.name());
        match.setMatchPhase(MatchPhase.FULL_TIME);
        match.setEndTime(LocalDateTime.now());
        notificationService.sendNotification("Match " + matchId + " has finished");
        return matchRepository.save(match);
    }

    public Match changePhase(String matchId, MatchPhase newPhase) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setMatchPhase(newPhase);
        notificationService.sendNotification("Match " + matchId + " has changed phase to " + newPhase.name());
        if (newPhase == MatchPhase.HALF_TIME) {
            notificationService.sendNotification("Half-time in match " + matchId);
        }
        return matchRepository.save(match);
    }

    public Match pauseMatch(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setStatus(MatchStatus.PAUSED.name());
        notificationService.sendNotification("Match " + matchId + " has been paused");
        return matchRepository.save(match);
    }

    public Match resumeMatch(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setStatus(MatchStatus.LIVE.name());
        notificationService.sendNotification("Match " + matchId + " has been resumed");
        return matchRepository.save(match);
    }

    public Match postponeMatch(String matchId, LocalDateTime newDate) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setStatus(MatchStatus.POSTPONED.name());
        match.setDate(newDate);
        notificationService.sendNotification("Match " + matchId + " has been postponed to " + newDate);
        return matchRepository.save(match);
    }

    public Match cancelMatch(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setStatus(MatchStatus.CANCELLED.name());
        notificationService.sendNotification("Match " + matchId + " has been cancelled");
        return matchRepository.save(match);
    }

    public Match addEvent(String matchId, Match.Event event) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        event.setId(UUID.randomUUID().toString());
        match.getEvents().add(event);

        if (EventType.GOAL.name().equals(event.getType())) {
            scoreManagementService.updateScore(matchId, event.getTeamId(), 1);
            notificationService.sendNotification("Goal scored in match " + matchId + " by team " + event.getTeamId());
        } else if (EventType.RED_CARD.name().equals(event.getType())) {
            notificationService.sendNotification("Red card in match " + matchId);
        }

        return matchRepository.save(match);
    }

    public Match deleteEvent(String matchId, String eventId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        Match.Event eventToRemove = match.getEvents().stream()
                .filter(event -> Objects.equals(event.getId(), eventId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        if (EventType.GOAL.name().equals(eventToRemove.getType())) {
            scoreManagementService.updateScore(matchId, eventToRemove.getTeamId(), -1);
            notificationService.sendNotification("Goal cancelled in match " + matchId);
        }

        match.getEvents().remove(eventToRemove);
        return matchRepository.save(match);
    }

    public Match updateEvent(String matchId, String eventId, Match.Event updatedEvent) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        Match.Event eventToUpdate = match.getEvents().stream()
                .filter(event -> Objects.equals(event.getId(), eventId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        // Logic to recalculate score if a goal event is modified
        if (EventType.GOAL.name().equals(eventToUpdate.getType()) && !EventType.GOAL.name().equals(updatedEvent.getType())) {
            // Goal was removed
            scoreManagementService.updateScore(matchId, eventToUpdate.getTeamId(), -1);
        } else if (!EventType.GOAL.name().equals(eventToUpdate.getType()) && EventType.GOAL.name().equals(updatedEvent.getType())) {
            // Goal was added
            scoreManagementService.updateScore(matchId, updatedEvent.getTeamId(), 1);
        } else if (EventType.GOAL.name().equals(eventToUpdate.getType()) && EventType.GOAL.name().equals(updatedEvent.getType()) && !eventToUpdate.getTeamId().equals(updatedEvent.getTeamId())) {
            // Goal team changed
            scoreManagementService.updateScore(matchId, eventToUpdate.getTeamId(), -1);
            scoreManagementService.updateScore(matchId, updatedEvent.getTeamId(), 1);
        }


        eventToUpdate.setMinute(updatedEvent.getMinute());
        eventToUpdate.setType(updatedEvent.getType());
        eventToUpdate.setTeamId(updatedEvent.getTeamId());
        eventToUpdate.setPlayerId(updatedEvent.getPlayerId());
        eventToUpdate.setDescription(updatedEvent.getDescription());

        return matchRepository.save(match);
    }

    public Match addAdditionalTime(String matchId, int additionalTime) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setAdditionalTime(additionalTime);
        return matchRepository.save(match);
    }

    public Page<Match.Event> getEvents(String matchId, Pageable pageable) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        List<Match.Event> events = match.getEvents();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), events.size());
        return new PageImpl<>(events.subList(start, end), pageable, events.size());
    }

    public Map<String, Integer> getFinalScore(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        return match.getScore();
    }

    public Map<Integer, Map<String, Integer>> getScoreHistory(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        Map<Integer, Map<String, Integer>> scoreHistory = new HashMap<>();
        Map<String, Integer> currentScore = new HashMap<>();
        currentScore.put(match.getTeam1Id(), 0);
        currentScore.put(match.getTeam2Id(), 0);

        // Add initial score
        scoreHistory.put(0, new HashMap<>(currentScore));

        match.getEvents().stream()
                .filter(event -> EventType.GOAL.name().equals(event.getType()))
                .sorted((e1, e2) -> Integer.compare(e1.getMinute(), e2.getMinute()))
                .forEach(event -> {
                    String teamId = event.getTeamId();
                    currentScore.put(teamId, currentScore.get(teamId) + 1);
                    scoreHistory.put(event.getMinute(), new HashMap<>(currentScore));
                });
        return scoreHistory;
    }


    public Match defineLineup(String matchId, String teamId, List<String> playerIds) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        if (teamId.equals(match.getTeam1Id())) {
            match.setTeam1Lineup(playerIds);
        } else if (teamId.equals(match.getTeam2Id())) {
            match.setTeam2Lineup(playerIds);
        } else {
            throw new IllegalArgumentException("Invalid team ID");
        }
        return matchRepository.save(match);
    }

    public Match defineSubstitutes(String matchId, String teamId, List<String> playerIds) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        if (teamId.equals(match.getTeam1Id())) {
            match.setTeam1Substitutes(playerIds);
        } else if (teamId.equals(match.getTeam2Id())) {
            match.setTeam2Substitutes(playerIds);
        } else {
            throw new IllegalArgumentException("Invalid team ID");
        }
        return matchRepository.save(match);
    }

    public Match defineTacticalSystem(String matchId, String teamId, String tacticalSystem) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        if (teamId.equals(match.getTeam1Id())) {
            match.setTeam1TacticalSystem(tacticalSystem);
        } else if (teamId.equals(match.getTeam2Id())) {
            match.setTeam2TacticalSystem(tacticalSystem);
        } else {
            throw new IllegalArgumentException("Invalid team ID");
        }
        return matchRepository.save(match);
    }

    public Match assignCaptain(String matchId, String teamId, String playerId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        if (teamId.equals(match.getTeam1Id())) {
            match.setTeam1CaptainId(playerId);
        } else if (teamId.equals(match.getTeam2Id())) {
            match.setTeam2CaptainId(playerId);
        } else {
            throw new IllegalArgumentException("Invalid team ID");
        }
        return matchRepository.save(match);
    }

    public Match recordCollectiveStats(String matchId, String teamId, Map<String, Object> stats) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.getCollectiveStats().put(teamId, stats);
        return matchRepository.save(match);
    }

    public Match recordIndividualStats(String matchId, List<Match.PlayerStats> playerStats) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setIndividualStats(playerStats);
        return matchRepository.save(match);
    }

    public Map<String, Object> calculateDerivedStats(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        // Placeholder for derived stats logic
        return new HashMap<>();
    }

    public Match addMedia(String matchId, Match.Media media) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        media.setId(UUID.randomUUID().toString());
        match.getMedia().add(media);
        return matchRepository.save(match);
    }

    public Match removeMedia(String matchId, String mediaId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.getMedia().removeIf(media -> media.getId().equals(mediaId));
        return matchRepository.save(match);
    }

    public Page<Match> searchMatches(SearchMatchesRequest request, Pageable pageable) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        if (request.getTeamId() != null) {
            criteria.add(new Criteria().orOperator(Criteria.where("team1Id").is(request.getTeamId()), Criteria.where("team2Id").is(request.getTeamId())));
        }
        if (request.getDate() != null) {
            criteria.add(Criteria.where("date").is(request.getDate()));
        }
        if (request.getStatus() != null) {
            criteria.add(Criteria.where("status").is(request.getStatus()));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        long count = mongoTemplate.count(query, Match.class);
        query.with(pageable);
        List<Match> matches = mongoTemplate.find(query, Match.class);

        return new PageImpl<>(matches, pageable, count);
    }

    public Match assignMainReferee(String matchId, String refereeName) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setMainReferee(refereeName);
        return matchRepository.save(match);
    }

    public Match assignAssistantReferees(String matchId, List<String> assistantReferees) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setAssistantReferees(assistantReferees);
        return matchRepository.save(match);
    }

    public Match assignFourthOfficial(String matchId, String fourthOfficial) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setFourthOfficial(fourthOfficial);
        return matchRepository.save(match);
    }

    public Match assignVarReferees(String matchId, List<String> varReferees) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setVarReferees(varReferees);
        return matchRepository.save(match);
    }

    public Page<Match> getTodaysMatches(Pageable pageable) {
        Query query = new Query();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().plusDays(1).atStartOfDay();
        query.addCriteria(Criteria.where("date").gte(startOfDay).lt(endOfDay));
        long count = mongoTemplate.count(query, Match.class);
        query.with(pageable);
        List<Match> matches = mongoTemplate.find(query, Match.class);
        return new PageImpl<>(matches, pageable, count);
    }

    public Page<Match> getUpcomingMatches(Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("date").gt(LocalDateTime.now()));
        long count = mongoTemplate.count(query, Match.class);
        query.with(pageable);
        List<Match> matches = mongoTemplate.find(query, Match.class);
        return new PageImpl<>(matches, pageable, count);
    }

    public Page<Match> getFinishedMatches(Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(MatchStatus.FINISHED.name()));
        long count = mongoTemplate.count(query, Match.class);
        query.with(pageable);
        List<Match> matches = mongoTemplate.find(query, Match.class);
        return new PageImpl<>(matches, pageable, count);
    }

    public Match addArbitralDecision(String matchId, Match.ArbitralDecision decision) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        decision.setId(UUID.randomUUID().toString());
        if (match.getDecisions() == null) {
            match.setDecisions(new ArrayList<>());
        }
        match.getDecisions().add(decision);
        return matchRepository.save(match);
    }

    public Page<Match.ArbitralDecision> getArbitralDecisions(String matchId, Pageable pageable) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        List<Match.ArbitralDecision> decisions = match.getDecisions();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), decisions.size());
        return new PageImpl<>(decisions.subList(start, end), pageable, decisions.size());
    }

    public long calculateTotalMatchDuration(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        if (match.getStartTime() == null || match.getEndTime() == null) {
            return 0;
        }
        return java.time.Duration.between(match.getStartTime(), match.getEndTime()).toMinutes();
    }

    public Page<Match.Event> getMatchTimeline(String matchId, Pageable pageable) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.getEvents().sort((e1, e2) -> Integer.compare(e1.getMinute(), e2.getMinute()));
        List<Match.Event> events = match.getEvents();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), events.size());
        return new PageImpl<>(events.subList(start, end), pageable, events.size());
    }

    public Match updateCurrentMinute(String matchId, int minute) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setCurrentMinute(minute);
        return matchRepository.save(match);
    }
}
