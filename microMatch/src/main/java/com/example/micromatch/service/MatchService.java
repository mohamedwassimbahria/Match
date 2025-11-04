package com.example.micromatch.service;

import com.example.micromatch.dto.SearchMatchesRequest;
import com.example.micromatch.entity.Match;
import com.example.micromatch.enums.EventType;
import com.example.micromatch.enums.MatchPhase;
import com.example.micromatch.enums.MatchStatus;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
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
        notificationService.sendNotification("Match " + matchId + " has started");
        return matchRepository.save(match);
    }

    public Match finishMatch(String matchId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setStatus(MatchStatus.FINISHED.name());
        match.setMatchPhase(MatchPhase.FULL_TIME);
        notificationService.sendNotification("Match " + matchId + " has finished");
        return matchRepository.save(match);
    }

    public Match changePhase(String matchId, MatchPhase newPhase) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.setMatchPhase(newPhase);
        notificationService.sendNotification("Match " + matchId + " has changed phase to " + newPhase.name());
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
            String teamId = event.getTeamId();
            match.getScore().put(teamId, match.getScore().get(teamId) + 1);
            notificationService.sendNotification("Goal scored in match " + matchId + " by team " + teamId);
        }

        return matchRepository.save(match);
    }

    public Match cancelGoal(String matchId, String eventId) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        Match.Event eventToRemove = match.getEvents().stream()
                .filter(event -> Objects.equals(event.getId(), eventId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        if (EventType.GOAL.name().equals(eventToRemove.getType())) {
            String teamId = eventToRemove.getTeamId();
            match.getScore().put(teamId, match.getScore().get(teamId) - 1);
            notificationService.sendNotification("Goal cancelled in match " + matchId);
        }

        match.getEvents().remove(eventToRemove);
        return matchRepository.save(match);
    }

    public Match updateScoreManually(String matchId, int scoreTeam1, int scoreTeam2) {
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchId));
        match.getScore().put(match.getTeam1Id(), scoreTeam1);
        match.getScore().put(match.getTeam2Id(), scoreTeam2);
        notificationService.sendNotification("Score updated manually for match " + matchId);
        return matchRepository.save(match);
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

    public List<Match> searchMatches(SearchMatchesRequest request) {
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

        return mongoTemplate.find(query, Match.class);
    }
}
