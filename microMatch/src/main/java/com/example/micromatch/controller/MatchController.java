package com.example.micromatch.controller;

import com.example.micromatch.dto.AddEventRequest;
import com.example.micromatch.dto.CreateMatchRequest;
import com.example.micromatch.dto.UpdateScoreRequest;
import com.example.micromatch.entity.Match;
import com.example.micromatch.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}/score")
    public Match updateScore(@PathVariable String id, @RequestBody UpdateScoreRequest request) {
        return matchService.updateScore(id, request.getTeamId(), request.getScore());
    }

    @PutMapping("/{id}/events")
    public Match addEvent(@PathVariable String id, @RequestBody AddEventRequest request) {
        return matchService.addEvent(id, request.getEvent());
    }
}
