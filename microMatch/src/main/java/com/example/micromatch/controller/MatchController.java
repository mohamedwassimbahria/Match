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
    public Match cancelGoal(@PathVariable String id, @PathVariable String eventId) {
        return matchService.cancelGoal(id, eventId);
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
}
