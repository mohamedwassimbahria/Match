package com.example.micromatch.controller;

import com.example.micromatch.dto.*;
import com.example.micromatch.entity.Match;
import com.example.micromatch.enums.MatchPhase;
import com.example.micromatch.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Optimized Match Controller with consolidated endpoints
 */
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    // ========== CRUD Operations ==========

    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody CreateMatchRequest request) {
        Match match = matchService.createMatch(request.getTeam1Id(), request.getTeam2Id(), request.getDate());
        return ResponseEntity.ok(match);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatch(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    // ========== Match Lifecycle ==========

    @PutMapping("/{id}/lifecycle/{action}")
    public ResponseEntity<Match> manageLifecycle(
            @PathVariable String id,
            @PathVariable String action,
            @RequestParam(required = false) String phase,
            @RequestParam(required = false) String newDate) {

        Match match;
        switch (action.toUpperCase()) {
            case "START":
                match = matchService.startMatch(id);
                break;
            case "FINISH":
                match = matchService.finishMatch(id);
                break;
            case "PAUSE":
                match = matchService.pauseMatch(id);
                break;
            case "RESUME":
                match = matchService.resumeMatch(id);
                break;
            case "CANCEL":
                match = matchService.cancelMatch(id);
                break;
            case "POSTPONE":
                if (newDate == null) throw new IllegalArgumentException("newDate is required for POSTPONE");
                match = matchService.postponeMatch(id, LocalDateTime.parse(newDate));
                break;
            case "CHANGE_PHASE":
                if (phase == null) throw new IllegalArgumentException("phase is required for CHANGE_PHASE");
                match = matchService.changePhase(id, MatchPhase.valueOf(phase));
                break;
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }
        return ResponseEntity.ok(match);
    }

    // ========== Consolidated Operations Endpoint ==========

    /**
     * Single endpoint for all match operations (replaces 15+ endpoints)
     * Operations: UPDATE_SCORE, UPDATE_MINUTE, ADD_TIME, SET_LINEUP, SET_SUBSTITUTES,
     *             SET_TACTICAL, SET_CAPTAIN, SET_MAIN_REFEREE, etc.
     */
    @PutMapping("/{id}/operations")
    public ResponseEntity<Match> performOperation(
            @PathVariable String id,
            @Valid @RequestBody MatchOperationRequest request) {

        Match match;
        String op = request.getOperation();

        switch (op) {
            // Timing operations
            case MatchOperationRequest.OP_UPDATE_MINUTE:
                match = matchService.updateCurrentMinute(id, request.getMinute());
                break;
            case MatchOperationRequest.OP_ADD_TIME:
                match = matchService.addAdditionalTime(id, request.getAdditionalTime());
                break;
            case MatchOperationRequest.OP_UPDATE_SCORE:
                match = matchService.updateScoreManually(id, request.getScoreTeam1(), request.getScoreTeam2());
                break;
            case MatchOperationRequest.OP_UPDATE_DATETIME:
                match = matchService.updateMatchDateTime(id, request.getNewDateTime());
                break;

            // Team setup operations
            case MatchOperationRequest.OP_SET_LINEUP:
                match = matchService.defineLineup(id, request.getTeamId(), request.getPlayerIds());
                break;
            case MatchOperationRequest.OP_SET_SUBSTITUTES:
                match = matchService.defineSubstitutes(id, request.getTeamId(), request.getPlayerIds());
                break;
            case MatchOperationRequest.OP_SET_TACTICAL:
                match = matchService.defineTacticalSystem(id, request.getTeamId(), request.getTacticalSystem());
                break;
            case MatchOperationRequest.OP_SET_CAPTAIN:
                match = matchService.assignCaptain(id, request.getTeamId(), request.getCaptainId());
                break;

            // Officials operations
            case MatchOperationRequest.OP_SET_MAIN_REFEREE:
                match = matchService.assignMainReferee(id, request.getOfficialName());
                break;
            case MatchOperationRequest.OP_SET_ASSISTANT_REFEREES:
                match = matchService.assignAssistantReferees(id, request.getOfficialNames());
                break;
            case MatchOperationRequest.OP_SET_FOURTH_OFFICIAL:
                match = matchService.assignFourthOfficial(id, request.getOfficialName());
                break;
            case MatchOperationRequest.OP_SET_VAR_REFEREES:
                match = matchService.assignVarReferees(id, request.getOfficialNames());
                break;

            // Event operations
            case MatchOperationRequest.OP_UPDATE_EVENT:
                match = matchService.updateEvent(id, request.getEventId(), request.getEvent());
                break;

            default:
                throw new IllegalArgumentException("Invalid operation: " + op);
        }

        return ResponseEntity.ok(match);
    }

    // ========== Events ==========

    @PostMapping("/{id}/events")
    public ResponseEntity<Match> addEvent(@PathVariable String id, @Valid @RequestBody Match.Event event) {
        return ResponseEntity.ok(matchService.addEvent(id, event));
    }

    @DeleteMapping("/{id}/events/{eventId}")
    public ResponseEntity<Match> deleteEvent(@PathVariable String id, @PathVariable String eventId) {
        return ResponseEntity.ok(matchService.deleteEvent(id, eventId));
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<PagedResponse<Match.Event>> getEvents(@PathVariable String id, Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.from(matchService.getEvents(id, pageable)));
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<PagedResponse<Match.Event>> getMatchTimeline(@PathVariable String id, Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.from(matchService.getMatchTimeline(id, pageable)));
    }

    // ========== Statistics ==========

    @PostMapping("/{id}/stats")
    public ResponseEntity<Match> recordStats(
            @PathVariable String id,
            @Valid @RequestBody MatchOperationRequest request) {

        Match match;
        if (request.getStats() != null && request.getTeamId() != null) {
            // Collective stats
            match = matchService.recordCollectiveStats(id, request.getTeamId(), request.getStats());
        } else if (request.getPlayerStats() != null) {
            // Individual stats
            match = matchService.recordIndividualStats(id, request.getPlayerStats());
        } else {
            throw new IllegalArgumentException("Either stats+teamId or playerStats must be provided");
        }

        return ResponseEntity.ok(match);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getDerivedStats(@PathVariable String id) {
        return ResponseEntity.ok(matchService.calculateDerivedStats(id));
    }

    // ========== Score ==========

    @GetMapping("/{id}/score")
    public ResponseEntity<Map<String, Integer>> getFinalScore(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getFinalScore(id));
    }

    @GetMapping("/{id}/score/history")
    public ResponseEntity<Map<Integer, Map<String, Integer>>> getScoreHistory(@PathVariable String id) {
        return ResponseEntity.ok(matchService.getScoreHistory(id));
    }

    // ========== Media ==========

    @PostMapping("/{id}/media")
    public ResponseEntity<Match> addMedia(@PathVariable String id, @Valid @RequestBody MatchOperationRequest request) {
        return ResponseEntity.ok(matchService.addMedia(id, request.getMedia()));
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public ResponseEntity<Match> removeMedia(@PathVariable String id, @PathVariable String mediaId) {
        return ResponseEntity.ok(matchService.removeMedia(id, mediaId));
    }

    // ========== Arbitral Decisions ==========

    @PostMapping("/{id}/decisions")
    public ResponseEntity<Match> addArbitralDecision(@PathVariable String id, @Valid @RequestBody MatchOperationRequest request) {
        return ResponseEntity.ok(matchService.addArbitralDecision(id, request.getDecision()));
    }

    @GetMapping("/{id}/decisions")
    public ResponseEntity<PagedResponse<Match.ArbitralDecision>> getArbitralDecisions(@PathVariable String id, Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.from(matchService.getArbitralDecisions(id, pageable)));
    }

    // ========== Search & Queries ==========

    @PostMapping("/search")
    public ResponseEntity<PagedResponse<Match>> searchMatches(
            @Valid @RequestBody SearchMatchesRequest request,
            Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.from(matchService.searchMatches(request, pageable)));
    }

    @GetMapping("/today")
    public ResponseEntity<PagedResponse<Match>> getTodaysMatches(Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.from(matchService.getTodaysMatches(pageable)));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<PagedResponse<Match>> getUpcomingMatches(Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.from(matchService.getUpcomingMatches(pageable)));
    }

    @GetMapping("/finished")
    public ResponseEntity<PagedResponse<Match>> getFinishedMatches(Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.from(matchService.getFinishedMatches(pageable)));
    }

    // ========== Utilities ==========

    @GetMapping("/{id}/duration")
    public ResponseEntity<Long> calculateTotalMatchDuration(@PathVariable String id) {
        return ResponseEntity.ok(matchService.calculateTotalMatchDuration(id));
    }
}
