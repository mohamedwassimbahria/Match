package com.example.micromatch.controller;

import com.example.micromatch.dto.*;
import com.example.micromatch.entity.Match;
import com.example.micromatch.entity.Planification;
import com.example.micromatch.service.PlanificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Optimized Planification Controller with consolidated endpoints
 */
@RestController
@RequestMapping("/api/v1/planifications")
@RequiredArgsConstructor
public class PlanificationController {

    private final PlanificationService planificationService;

    // ========== CRUD Operations ==========

    @PostMapping
    public ResponseEntity<Planification> createPlanification(@Valid @RequestBody CreatePlanificationRequest request) {
        Planification planification = planificationService.createPlanification(request.getMatchId(), request.getDatePropose());
        return ResponseEntity.ok(planification);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planification> getPlanification(@PathVariable String id) {
        return ResponseEntity.ok(planificationService.getPlanificationById(id));
    }

    // ========== Consolidated Operations Endpoint ==========

    /**
     * Single endpoint for all planification operations (replaces 20+ endpoints)
     * Operations: UPDATE_SCHEDULE, SET_ARRIVALS, ASSESS_RISK, SET_REVENUE, etc.
     */
    @PutMapping("/{id}/operations")
    public ResponseEntity<Planification> performOperation(
            @PathVariable String id,
            @Valid @RequestBody PlanificationOperationRequest request) {

        Planification planification;
        String op = request.getOperation();

        switch (op) {
            // Schedule operations
            case PlanificationOperationRequest.OP_UPDATE_SCHEDULE:
                planification = planificationService.updateDetailedSchedule(id, request.getSchedule());
                break;
            case PlanificationOperationRequest.OP_SET_ARRIVALS:
                planification = planificationService.defineTeamArrivals(id, request.getTeamArrivals());
                break;
            case PlanificationOperationRequest.OP_SET_TECHNICAL_MEETING:
                planification = planificationService.planTechnicalMeeting(id, request.getTechnicalMeeting());
                break;
            case PlanificationOperationRequest.OP_SET_PRESS_CONFERENCE:
                planification = planificationService.planPressConference(id, request.getPressConference());
                break;
            case PlanificationOperationRequest.OP_SET_WARMUPS:
                planification = planificationService.planWarmUps(id, request.getWarmUps());
                break;

            // Security and revenue operations
            case PlanificationOperationRequest.OP_ASSESS_RISK:
                planification = planificationService.assessRiskAndSecurity(id, request.getRiskLevel(),
                        request.getSecurityNeeds(), request.getEstimatedAttendance());
                break;
            case PlanificationOperationRequest.OP_SET_REVENUE:
                planification = planificationService.estimatePotentialRevenue(id, request.getPotentialRevenue());
                break;

            // Contingency operations
            case PlanificationOperationRequest.OP_SET_CONTINGENCY:
                planification = planificationService.manageContingency(id, request.getContingencyPlan(),
                        request.getLastMinuteReport());
                break;
            case PlanificationOperationRequest.OP_SET_CATCHUP_DATE:
                planification = planificationService.findCatchUpDate(id, request.getCatchUpDate(),
                        request.getReason());
                break;

            // Date/time operations
            case PlanificationOperationRequest.OP_UPDATE_DATETIME:
                planification = planificationService.updateMatchDateTime(id, request.getNewDateTime());
                break;
            case PlanificationOperationRequest.OP_CHECK_CONSTRAINTS:
                planification = planificationService.checkAllConstraints(id, request.getDate());
                break;

            default:
                throw new IllegalArgumentException("Invalid operation: " + op);
        }

        return ResponseEntity.ok(planification);
    }

    // ========== Workflow Management ==========

    /**
     * Consolidated workflow endpoint
     * Actions: SUBMIT, APPROVE, REJECT, REQUEST_MODIFICATION, CONFIRM, CANCEL, VALIDATE, LOCK
     */
    @PutMapping("/{id}/workflow/{action}")
    public ResponseEntity<Planification> manageWorkflow(
            @PathVariable String id,
            @PathVariable String action,
            @RequestParam(required = false) String reason) {

        Planification planification;
        switch (action.toUpperCase()) {
            case "SUBMIT":
                planification = planificationService.submitForValidation(id);
                break;
            case "APPROVE":
                planification = planificationService.approvePlanification(id);
                break;
            case "REJECT":
                if (reason == null) throw new IllegalArgumentException("reason is required for REJECT");
                planification = planificationService.rejectPlanification(id, reason);
                break;
            case "REQUEST_MODIFICATION":
                if (reason == null) throw new IllegalArgumentException("reason is required for REQUEST_MODIFICATION");
                planification = planificationService.requestModification(id, reason);
                break;
            case "CONFIRM":
                planification = planificationService.confirmPlanification(id);
                break;
            case "CANCEL":
                planification = planificationService.cancelPlanification(id);
                break;
            case "VALIDATE":
                planification = planificationService.validatePlanning(id);
                break;
            case "LOCK":
                planification = planificationService.lockPlanning(id);
                break;
            default:
                throw new IllegalArgumentException("Invalid workflow action: " + action);
        }

        return ResponseEntity.ok(planification);
    }

    // ========== Constraints & Analysis ==========

    @GetMapping("/{id}/constraints/calendar")
    public ResponseEntity<Planification> checkCalendarConflicts(@PathVariable String id) {
        return ResponseEntity.ok(planificationService.checkCalendarConflicts(id));
    }

    @GetMapping("/{id}/constraints/tv")
    public ResponseEntity<Planification> checkTvConstraints(@PathVariable String id) {
        return ResponseEntity.ok(planificationService.checkTvConstraints(id));
    }

    @GetMapping("/{id}/constraints/rest-period")
    public ResponseEntity<ApiResponse<String>> checkTeamRestPeriod(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(planificationService.checkTeamRestPeriod(id)));
    }

    // ========== Date Proposals ==========

    @PostMapping("/{id}/proposals")
    public ResponseEntity<Planification> generateDateProposals(@PathVariable String id) {
        return ResponseEntity.ok(planificationService.generateDateProposals(id));
    }

    @GetMapping("/{id}/best-date")
    public ResponseEntity<LocalDateTime> proposeBestAvailableDate(@PathVariable String id) {
        return ResponseEntity.ok(planificationService.proposeBestAvailableDate(id));
    }

    @GetMapping("/{id}/alternatives")
    public ResponseEntity<List<LocalDateTime>> suggestAlternatives(@PathVariable String id) {
        return ResponseEntity.ok(planificationService.suggestAlternatives(id));
    }

    // ========== Queries ==========

    @GetMapping("/history/{matchId}")
    public ResponseEntity<PagedResponse<Planification>> getPlanificationHistory(
            @PathVariable String matchId,
            Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.from(planificationService.getPlanificationHistory(matchId, pageable)));
    }

    // ========== Championship Planning ==========

    @PostMapping("/championship")
    public ResponseEntity<List<Match>> scheduleChampionship(@RequestParam List<String> teamIds) {
        return ResponseEntity.ok(planificationService.scheduleMatches(teamIds));
    }

    // ========== Notifications ==========

    @PostMapping("/{id}/notify")
    public ResponseEntity<ApiResponse<Void>> notifyStakeholders(
            @PathVariable String id,
            @RequestParam String message) {
        planificationService.notifyStakeholders(id, message);
        return ResponseEntity.ok(ApiResponse.success("Notification sent successfully", null));
    }
}
