package com.example.micromatch.controller;

import com.example.micromatch.dto.*;
import com.example.micromatch.entity.Match;
import com.example.micromatch.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public Match createMatch(@RequestBody CreateMatchRequest request) {
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
    public Match changePhase(@PathVariable String id, @RequestBody ChangePhaseRequest request) {
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
    public Match postponeMatch(@PathVariable String id, @RequestBody PostponeMatchRequest request) {
        return matchService.postponeMatch(id, request.getNewDate());
    }

    @PutMapping("/{id}/cancel")
    public Match cancelMatch(@PathVariable String id) {
        return matchService.cancelMatch(id);
    }

    @PostMapping("/{id}/events")
    public Match addEvent(@PathVariable String id, @RequestBody Match.Event event) {
        return matchService.addEvent(id, event);
    }

    @DeleteMapping("/{id}/events/{eventId}")
    public Match deleteEvent(@PathVariable String id, @PathVariable String eventId) {
        return matchService.deleteEvent(id, eventId);
    }

    @PutMapping("/{id}/events/{eventId}")
    public Match updateEvent(@PathVariable String id, @PathVariable String eventId, @RequestBody UpdateEventRequest request) {
        return matchService.updateEvent(id, eventId, request.getEvent());
    }

    @GetMapping("/{id}/events")
    public List<Match.Event> getEvents(@PathVariable String id) {
        return matchService.getEvents(id);
    }

    @PutMapping("/{id}/additional-time")
    public Match addAdditionalTime(@PathVariable String id, @RequestBody AddAdditionalTimeRequest request) {
        return matchService.addAdditionalTime(id, request.getAdditionalTime());
    }

    @GetMapping("/{id}/score/final")
    public Map<String, Integer> getFinalScore(@PathVariable String id) {
        return matchService.getFinalScore(id);
    }

    @GetMapping("/{id}/score/history")
    public Map<Integer, Map<String, Integer>> getScoreHistory(@PathVariable String id) {
        return matchService.getScoreHistory(id);
    }

    @PutMapping("/{id}/score/manual")
    public Match updateScoreManually(@PathVariable String id, @RequestBody UpdateScoreManuallyRequest request) {
        return matchService.updateScoreManually(id, request.getScoreTeam1(), request.getScoreTeam2());
    }

    @PutMapping("/{id}/lineup")
    public Match defineLineup(@PathVariable String id, @RequestBody DefineLineupRequest request) {
        return matchService.defineLineup(id, request.getTeamId(), request.getPlayerIds());
    }

    @PutMapping("/{id}/substitutes")
    public Match defineSubstitutes(@PathVariable String id, @RequestBody DefineSubstitutesRequest request) {
        return matchService.defineSubstitutes(id, request.getTeamId(), request.getPlayerIds());
    }

    @PutMapping("/{id}/tactical-system")
    public Match defineTacticalSystem(@PathVariable String id, @RequestBody DefineTacticalSystemRequest request) {
        return matchService.defineTacticalSystem(id, request.getTeamId(), request.getTacticalSystem());
    }

    @PutMapping("/{id}/captain")
    public Match assignCaptain(@PathVariable String id, @RequestBody AssignCaptainRequest request) {
        return matchService.assignCaptain(id, request.getTeamId(), request.getPlayerId());
    }

    @PostMapping("/{id}/stats/collective")
    public Match recordCollectiveStats(@PathVariable String id, @RequestBody RecordCollectiveStatsRequest request) {
        return matchService.recordCollectiveStats(id, request.getTeamId(), request.getStats());
    }

    @PostMapping("/{id}/stats/individual")
    public Match recordIndividualStats(@PathVariable String id, @RequestBody RecordIndividualStatsRequest request) {
        return matchService.recordIndividualStats(id, request.getPlayerStats());
    }

    @GetMapping("/{id}/stats/derived")
    public Map<String, Object> getDerivedStats(@PathVariable String id) {
        return matchService.calculateDerivedStats(id);
    }

    @PostMapping("/{id}/media")
    public Match addMedia(@PathVariable String id, @RequestBody AddMediaRequest request) {
        return matchService.addMedia(id, request.getMedia());
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public Match removeMedia(@PathVariable String id, @PathVariable String mediaId) {
        return matchService.removeMedia(id, mediaId);
    }

    @PostMapping("/search")
    public List<Match> searchMatches(@RequestBody SearchMatchesRequest request) {
        return matchService.searchMatches(request);
    }

    @PutMapping("/{id}/referee/main")
    public Match assignMainReferee(@PathVariable String id, @RequestBody AssignMainRefereeRequest request) {
        return matchService.assignMainReferee(id, request.getRefereeName());
    }

    @PutMapping("/{id}/referee/assistants")
    public Match assignAssistantReferees(@PathVariable String id, @RequestBody AssignAssistantRefereesRequest request) {
        return matchService.assignAssistantReferees(id, request.getAssistantReferees());
    }

    @PutMapping("/{id}/referee/fourth-official")
    public Match assignFourthOfficial(@PathVariable String id, @RequestBody AssignFourthOfficialRequest request) {
        return matchService.assignFourthOfficial(id, request.getFourthOfficial());
    }

    @PutMapping("/{id}/referee/var")
    public Match assignVarReferees(@PathVariable String id, @RequestBody AssignVarRefereesRequest request) {
        return matchService.assignVarReferees(id, request.getVarReferees());
    }

    @GetMapping("/today")
    public List<Match> getTodaysMatches() {
        return matchService.getTodaysMatches();
    }

    @GetMapping("/upcoming")
    public List<Match> getUpcomingMatches() {
        return matchService.getUpcomingMatches();
    }

    @GetMapping("/finished")
    public List<Match> getFinishedMatches() {
        return matchService.getFinishedMatches();
    }

    @PostMapping("/{id}/decisions")
    public Match addArbitralDecision(@PathVariable String id, @RequestBody AddArbitralDecisionRequest request) {
        return matchService.addArbitralDecision(id, request.getDecision());
    }

    @GetMapping("/{id}/decisions")
    public List<Match.ArbitralDecision> getArbitralDecisions(@PathVariable String id) {
        return matchService.getArbitralDecisions(id);
    }

    @GetMapping("/{id}/duration")
    public long calculateTotalMatchDuration(@PathVariable String id) {
        return matchService.calculateTotalMatchDuration(id);
    }

    @GetMapping("/{id}/timeline")
    public List<Match.Event> getMatchTimeline(@PathVariable String id) {
        return matchService.getMatchTimeline(id);
    }

    @PutMapping("/{id}/current-minute")
    public Match updateCurrentMinute(@PathVariable String id, @RequestBody UpdateCurrentMinuteRequest request) {
        return matchService.updateCurrentMinute(id, request.getMinute());
    }
}
