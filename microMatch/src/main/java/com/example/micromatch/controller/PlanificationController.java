package com.example.micromatch.controller;

import com.example.micromatch.dto.*;
import com.example.micromatch.entity.Planification;
import com.example.micromatch.service.PlanificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/planifications")
@RequiredArgsConstructor
public class PlanificationController {

    private final PlanificationService planificationService;

    @PostMapping
    public Planification createPlanification(@Valid @RequestBody CreatePlanificationRequest request) {
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
    public Planification checkAllConstraints(@PathVariable String id, @Valid @RequestBody CheckConstraintsRequest request) {
        return planificationService.checkAllConstraints(id, request.getDate());
    }

    @PutMapping("/{id}/schedule")
    public Planification updateDetailedSchedule(@PathVariable String id, @Valid @RequestBody UpdateDetailedScheduleRequest request) {
        return planificationService.updateDetailedSchedule(id, request.getSchedule());
    }

    @PutMapping("/{id}/team-arrivals")
    public Planification defineTeamArrivals(@PathVariable String id, @Valid @RequestBody DefineTeamArrivalsRequest request) {
        return planificationService.defineTeamArrivals(id, request.getTeamArrivals());
    }

    @PutMapping("/{id}/technical-meeting")
    public Planification planTechnicalMeeting(@PathVariable String id, @Valid @RequestBody PlanTechnicalMeetingRequest request) {
        return planificationService.planTechnicalMeeting(id, request.getTechnicalMeeting());
    }

    @PutMapping("/{id}/press-conference")
    public Planification planPressConference(@PathVariable String id, @Valid @RequestBody PlanPressConferenceRequest request) {
        return planificationService.planPressConference(id, request.getPressConference());
    }

    @GetMapping("/{id}/full-day-schedule")
    public String generateFullDaySchedule(@PathVariable String id) {
        return planificationService.generateFullDaySchedule(id);
    }

    @GetMapping("/history/{matchId}")
    public Page<Planification> getPlanificationHistory(@PathVariable String matchId, Pageable pageable) {
        return planificationService.getPlanificationHistory(matchId, pageable);
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
    public Planification assessRiskAndSecurity(@PathVariable String id, @Valid @RequestBody AssessRiskAndSecurityRequest request) {
        return planificationService.assessRiskAndSecurity(id, request.getRiskLevel(), request.getSecurityNeeds(), request.getEstimatedAttendance());
    }

    @PutMapping("/{id}/revenue")
    public Planification estimatePotentialRevenue(@PathVariable String id, @Valid @RequestBody EstimatePotentialRevenueRequest request) {
        return planificationService.estimatePotentialRevenue(id, request.getPotentialRevenue());
    }

    @PutMapping("/{id}/contingency")
    public Planification manageContingency(@PathVariable String id, @Valid @RequestBody ManageContingencyRequest request) {
        return planificationService.manageContingency(id, request.getContingencyPlan(), request.getLastMinuteReport());
    }

    @PutMapping("/{id}/catch-up-date")
    public Planification findCatchUpDate(@PathVariable String id, @Valid @RequestBody FindCatchUpDateRequest request) {
        return planificationService.findCatchUpDate(id, request.getCatchUpDate(), request.getReasonForChange());
    }

    @PutMapping("/{id}/details")
    public Planification updatePlanificationDetails(@PathVariable String id, @Valid @RequestBody UpdatePlanificationDetailsRequest request) {
        return planificationService.updatePlanificationDetails(id, request);
    }

    @PostMapping("/{id}/proposals")
    public Planification generateDateProposals(@PathVariable String id) {
        return planificationService.generateDateProposals(id);
    }

    @PutMapping("/{id}/validate")
    public Planification validatePlanning(@PathVariable String id) {
        return planificationService.validatePlanning(id);
    }

    @PutMapping("/{id}/datetime")
    public Planification updateMatchDateTime(@PathVariable String id, @Valid @RequestBody UpdateMatchDateTimeRequest request) {
        return planificationService.updateMatchDateTime(id, request.getNewDateTime());
    }

    @GetMapping("/{id}/rest-period")
    public String checkTeamRestPeriod(@PathVariable String id) {
        return planificationService.checkTeamRestPeriod(id);
    }

    @GetMapping("/{id}/calendar-conflicts")
    public Planification checkCalendarConflicts(@PathVariable String id) {
        return planificationService.checkCalendarConflicts(id);
    }

    @GetMapping("/{id}/tv-constraints")
    public Planification checkTvConstraints(@PathVariable String id) {
        return planificationService.checkTvConstraints(id);
    }

    @GetMapping("/{id}/best-date")
    public java.time.LocalDateTime proposeBestAvailableDate(@PathVariable String id) {
        return planificationService.proposeBestAvailableDate(id);
    }

    @GetMapping("/{id}/alternatives")
    public List<java.time.LocalDateTime> suggestAlternatives(@PathVariable String id) {
        return planificationService.suggestAlternatives(id);
    }

    @PutMapping("/{id}/submit")
    public Planification submitForValidation(@PathVariable String id) {
        return planificationService.submitForValidation(id);
    }

    @PutMapping("/{id}/approve")
    public Planification approvePlanification(@PathVariable String id) {
        return planificationService.approvePlanification(id);
    }

    @PutMapping("/{id}/reject")
    public Planification rejectPlanification(@PathVariable String id, @Valid @RequestBody ReasonRequest request) {
        return planificationService.rejectPlanification(id, request.getReason());
    }

    @PutMapping("/{id}/request-modification")
    public Planification requestModification(@PathVariable String id, @Valid @RequestBody ReasonRequest request) {
        return planificationService.requestModification(id, request.getReason());
    }

    @PostMapping("/{id}/notify-stakeholders")
    public void notifyStakeholders(@PathVariable String id, @Valid @RequestBody String message) {
        planificationService.notifyStakeholders(id, message);
    }

    @PutMapping("/{id}/warm-ups")
    public Planification planWarmUps(@PathVariable String id, @Valid @RequestBody PlanWarmUpsRequest request) {
        return planificationService.planWarmUps(id, request.getWarmUps());
    }

    @GetMapping("/{id}/calendar-impact")
    public String analyzeGlobalCalendarImpact(@PathVariable String id) {
        return planificationService.analyzeGlobalCalendarImpact(id);
    }

    @PutMapping("/{id}/lock")
    public Planification lockPlanning(@PathVariable String id) {
        return planificationService.lockPlanning(id);
    }

    @PostMapping("/championship")
    public String planChampionship(@Valid @RequestBody PlanChampionshipRequest request) {
        return planificationService.planChampionship(request.getTeamIds());
    }

    @PutMapping("/{id}/mark-confirmed")
    public Planification markAsConfirmed(@PathVariable String id) {
        return planificationService.markAsConfirmed(id);
    }
}
