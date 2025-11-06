package com.example.micromatch.controller;

import com.example.micromatch.dto.*;
import com.example.micromatch.entity.Match;
import com.example.micromatch.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public Match createMatch(@Valid @RequestBody CreateMatchRequest request) {
        return matchService.createMatch(request.getTeam1Id(), request.getTeam2Id(), request.getDate());
    }

    @PutMapping("/{id}/start")
    public Match startMatch(@PathVariable String id) {
        return matchService.startMatch(id);
    }

    @PutMapping("/{id}/finish")
    public Match finishMatch(@PathVariable String id) {
        return matchService.finishMatch(id);
    }

    @PutMapping("/{id}/phase")
    public Match changePhase(@PathVariable String id, @Valid @RequestBody ChangePhaseRequest request) {
        return matchService.changePhase(id, request.getNewPhase());
    }

    @PutMapping("/{id}/pause")
    public Match pauseMatch(@PathVariable String id) {
        return matchService.pauseMatch(id);
    }

    @PutMapping("/{id}/resume")
    public Match resumeMatch(@PathVariable String id) {
        return matchService.resumeMatch(id);
    }

    @PutMapping("/{id}/postpone")
    public Match postponeMatch(@PathVariable String id, @Valid @RequestBody PostponeMatchRequest request) {
        return matchService.postponeMatch(id, request.getNewDate());
    }

    @PutMapping("/{id}/cancel")
    public Match cancelMatch(@PathVariable String id) {
        return matchService.cancelMatch(id);
    }

    @PostMapping("/{id}/events")
    public Match addEvent(@PathVariable String id, @Valid @RequestBody Match.Event event) {
        return matchService.addEvent(id, event);
    }

    @DeleteMapping("/{id}/events/{eventId}")
    public Match deleteEvent(@PathVariable String id, @PathVariable String eventId) {
        return matchService.deleteEvent(id, eventId);
    }

    @GetMapping("/{id}/events")
    public Page<Match.Event> getEvents(@PathVariable String id, Pageable pageable) {
        return matchService.getEvents(id, pageable);
    }

    @PutMapping("/{id}/update")
    public Match updateMatch(@PathVariable String id, @Valid @RequestBody MatchUpdateRequest request) {
        switch (request.getUpdateType()) {
            case ADD_ADDITIONAL_TIME:
                return matchService.addAdditionalTime(id, request.getAdditionalTime());
            case UPDATE_CURRENT_MINUTE:
                return matchService.updateCurrentMinute(id, request.getMinute());
            case UPDATE_SCORE:
                return matchService.updateScoreManually(id, request.getScoreTeam1(), request.getScoreTeam2());
            case UPDATE_EVENT:
                return matchService.updateEvent(id, request.getEventId(), request.getEvent());
            case UPDATE_DATETIME:
                return matchService.updateMatchDateTime(id, request.getNewDateTime());
            default:
                throw new IllegalArgumentException("Invalid update type");
        }
    }

    @GetMapping("/{id}/score/final")
    public Map<String, Integer> getFinalScore(@PathVariable String id) {
        return matchService.getFinalScore(id);
    }

    @GetMapping("/{id}/score/history")
    public Map<Integer, Map<String, Integer>> getScoreHistory(@PathVariable String id) {
        return matchService.getScoreHistory(id);
    }

    @PutMapping("/{id}/team-setup")
    public Match teamSetup(@PathVariable String id, @Valid @RequestBody TeamSetupRequest request) {
        switch (request.getSetupType()) {
            case LINEUP:
                return matchService.defineLineup(id, request.getTeamId(), request.getPlayerIds());
            case SUBSTITUTES:
                return matchService.defineSubstitutes(id, request.getTeamId(), request.getPlayerIds());
            case TACTICAL_SYSTEM:
                return matchService.defineTacticalSystem(id, request.getTeamId(), request.getTacticalSystem());
            default:
                throw new IllegalArgumentException("Invalid setup type");
        }
    }

    @PutMapping("/{id}/assign-personnel")
    public Match assignPersonnel(@PathVariable String id, @Valid @RequestBody AssignPersonnelRequest request) {
        switch (request.getRole()) {
            case MAIN_REFEREE:
                return matchService.assignMainReferee(id, request.getName());
            case ASSISTANT_REFEREE:
                return matchService.assignAssistantReferees(id, request.getNames());
            case FOURTH_OFFICIAL:
                return matchService.assignFourthOfficial(id, request.getName());
            case VAR_REFEREE:
                return matchService.assignVarReferees(id, request.getNames());
            case CAPTAIN:
                return matchService.assignCaptain(id, request.getTeamId(), request.getPlayerId());
            default:
                throw new IllegalArgumentException("Invalid personnel role");
        }
    }

    @PostMapping("/{id}/stats/collective")
    public Match recordCollectiveStats(@PathVariable String id, @Valid @RequestBody RecordCollectiveStatsRequest request) {
        return matchService.recordCollectiveStats(id, request.getTeamId(), request.getStats());
    }

    @PostMapping("/{id}/stats/individual")
    public Match recordIndividualStats(@PathVariable String id, @Valid @RequestBody RecordIndividualStatsRequest request) {
        return matchService.recordIndividualStats(id, request.getPlayerStats());
    }

    @GetMapping("/{id}/stats/derived")
    public Map<String, Object> getDerivedStats(@PathVariable String id) {
        return matchService.calculateDerivedStats(id);
    }

    @PostMapping("/{id}/media")
    public Match addMedia(@PathVariable String id, @Valid @RequestBody AddMediaRequest request) {
        return matchService.addMedia(id, request.getMedia());
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public Match removeMedia(@PathVariable String id, @PathVariable String mediaId) {
        return matchService.removeMedia(id, mediaId);
    }

    @PostMapping("/search")
    public Page<Match> searchMatches(@Valid @RequestBody SearchMatchesRequest request, Pageable pageable) {
        return matchService.searchMatches(request, pageable);
    }

    @GetMapping("/today")
    public Page<Match> getTodaysMatches(Pageable pageable) {
        return matchService.getTodaysMatches(pageable);
    }

    @GetMapping("/upcoming")
    public Page<Match> getUpcomingMatches(Pageable pageable) {
        return matchService.getUpcomingMatches(pageable);
    }

    @GetMapping("/finished")
    public Page<Match> getFinishedMatches(Pageable pageable) {
        return matchService.getFinishedMatches(pageable);
    }

    @PostMapping("/{id}/decisions")
    public Match addArbitralDecision(@PathVariable String id, @Valid @RequestBody AddArbitralDecisionRequest request) {
        return matchService.addArbitralDecision(id, request.getDecision());
    }

    @GetMapping("/{id}/decisions")
    public Page<Match.ArbitralDecision> getArbitralDecisions(@PathVariable String id, Pageable pageable) {
        return matchService.getArbitralDecisions(id, pageable);
    }

    @GetMapping("/{id}/duration")
    public long calculateTotalMatchDuration(@PathVariable String id) {
        return matchService.calculateTotalMatchDuration(id);
    }

    @GetMapping("/{id}/timeline")
    public Page<Match.Event> getMatchTimeline(@PathVariable String id, Pageable pageable) {
        return matchService.getMatchTimeline(id, pageable);
    }
}
