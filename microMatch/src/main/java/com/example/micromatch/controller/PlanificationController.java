package com.example.micromatch.controller;

import com.example.micromatch.dto.CheckConstraintsRequest;
import com.example.micromatch.dto.CreatePlanificationRequest;
import com.example.micromatch.dto.UpdateDetailedScheduleRequest;
import com.example.micromatch.entity.Planification;
import com.example.micromatch.service.PlanificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/planifications")
@RequiredArgsConstructor
public class PlanificationController {

    private final PlanificationService planificationService;

    @PostMapping
    public Planification createPlanification(@RequestBody CreatePlanificationRequest request) {
        return planificationService.createPlanification(request.getMatchId(), request.getDatePropose());
    }

    @PutMapping("/{id}/confirm")
    public Planification confirmPlanification(@PathVariable String id) {
        return planificationService.confirmPlanification(id);
    }

    @PutMapping("/{id}/cancel")
    public Planification cancelPlanification(@PathVariable String id) {
        return planificationService.cancelPlanification(id);
    }

    @PostMapping("/{id}/constraints/check")
    public Planification checkAllConstraints(@PathVariable String id, @RequestBody CheckConstraintsRequest request) {
        return planificationService.checkAllConstraints(id, request.getDate());
    }

    @PutMapping("/{id}/schedule")
    public Planification updateDetailedSchedule(@PathVariable String id, @RequestBody UpdateDetailedScheduleRequest request) {
        return planificationService.updateDetailedSchedule(id, request.getSchedule());
    }
}
