package com.example.micromatch.controller;

import com.example.micromatch.dto.*;
import com.example.micromatch.entity.Planification;
import com.example.micromatch.service.PlanificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{id}/team-arrivals")
    public Planification defineTeamArrivals(@PathVariable String id, @RequestBody DefineTeamArrivalsRequest request) {
        return planificationService.defineTeamArrivals(id, request.getTeamArrivals());
    }

    @PutMapping("/{id}/technical-meeting")
    public Planification planTechnicalMeeting(@PathVariable String id, @RequestBody PlanTechnicalMeetingRequest request) {
        return planificationService.planTechnicalMeeting(id, request.getTechnicalMeeting());
    }

    @PutMapping("/{id}/press-conference")
    public Planification planPressConference(@PathVariable String id, @RequestBody PlanPressConferenceRequest request) {
        return planificationService.planPressConference(id, request.getPressConference());
    }

    @GetMapping("/{id}/full-day-schedule")
    public String generateFullDaySchedule(@PathVariable String id) {
        return planificationService.generateFullDaySchedule(id);
    }

    @GetMapping("/history/{matchId}")
    public List<Planification> getPlanificationHistory(@PathVariable String matchId) {
        return planificationService.getPlanificationHistory(matchId);
    }

    @GetMapping("/{id}/report")
    public String generatePlanificationReport(@PathVariable String id) {
        return planificationService.generatePlanificationReport(id);
    }

    @GetMapping("/{id}/export")
    public String exportPlanificationData(@PathVariable String id) {
        return planificationService.exportPlanificationData(id);
    }

    @PutMapping("/{id}/risk-security")
    public Planification assessRiskAndSecurity(@PathVariable String id, @RequestBody AssessRiskAndSecurityRequest request) {
        return planificationService.assessRiskAndSecurity(id, request.getRiskLevel(), request.getSecurityNeeds(), request.getEstimatedAttendance());
    }

    @PutMapping("/{id}/revenue")
    public Planification estimatePotentialRevenue(@PathVariable String id, @RequestBody EstimatePotentialRevenueRequest request) {
        return planificationService.estimatePotentialRevenue(id, request.getPotentialRevenue());
    }

    @PutMapping("/{id}/contingency")
    public Planification manageContingency(@PathVariable String id, @RequestBody ManageContingencyRequest request) {
        return planificationService.manageContingency(id, request.getContingencyPlan(), request.getLastMinuteReport());
    }

    @PutMapping("/{id}/catch-up-date")
    public Planification findCatchUpDate(@PathVariable String id, @RequestBody FindCatchUpDateRequest request) {
        return planificationService.findCatchUpDate(id, request.getCatchUpDate(), request.getReasonForChange());
    }
}
