package com.example.micromatch.service;

import com.example.micromatch.entity.Planification;
import com.example.micromatch.enums.PlanificationStatus;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.MatchRepository;
import com.example.micromatch.repository.PlanificationRepository;
import com.example.micromatch.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlanificationService {

    private final PlanificationRepository planificationRepository;
    private final NotificationService notificationService;
    private final MatchRepository matchRepository;

    /**
     * Get a planification by ID
     */
    public Planification getPlanificationById(String planificationId) {
        return planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
    }

    /**
     * Create a new planification for a match
     */
    public Planification createPlanification(String matchId, LocalDateTime datePropose) {
        Planification planification = Planification.builder()
                .matchId(matchId)
                .datePropose(datePropose)
                .statut(PlanificationStatus.PROPOSED.name())
                .auditInfo(Planification.AuditInfo.builder()
                        .historiqueModifications(new ArrayList<>())
                        .build())
                .workflowInfo(Planification.WorkflowInfo.builder()
                        .validated(false)
                        .planningLocked(false)
                        .build())
                .constraints(Planification.PlanificationConstraints.builder().build())
                .securityAndRevenue(Planification.SecurityAndRevenue.builder().build())
                .matchDaySchedule(Planification.MatchDaySchedule.builder()
                        .detailedSchedule(new HashMap<>())
                        .build())
                .contingencyInfo(Planification.ContingencyInfo.builder().build())
                .build();
        notificationService.sendNotification("Planification created for match " + matchId);
        return planificationRepository.save(planification);
    }

    public Planification confirmPlanification(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setStatut(PlanificationStatus.CONFIRMED.name());
        notificationService.sendNotification("Planification " + planificationId + " has been confirmed");
        return planificationRepository.save(planification);
    }

    public Planification cancelPlanification(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setStatut(PlanificationStatus.CANCELLED.name());
        notificationService.sendNotification("Planification " + planificationId + " has been cancelled");
        return planificationRepository.save(planification);
    }

    /**
     * Check all constraints for a planification
     */
    public Planification checkAllConstraints(String planificationId, LocalDateTime date) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getConstraints() == null) {
            planification.setConstraints(Planification.PlanificationConstraints.builder().build());
        }

        // Check constraints (placeholder logic - to be improved)
        planification.getConstraints().setStadiumAvailable(true);
        planification.getConstraints().setTeam1MinRestDays(3);
        planification.getConstraints().setTeam2MinRestDays(3);
        planification.getConstraints().setTvBlackout(false);
        planification.getConstraints().setInternationalBreak(false);

        notificationService.sendNotification("Constraints checked for planification " + planificationId);
        return planificationRepository.save(planification);
    }

    /**
     * Update detailed schedule for match day
     */
    public Planification updateDetailedSchedule(String planificationId, Map<String, LocalDateTime> schedule) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getMatchDaySchedule() == null) {
            planification.setMatchDaySchedule(Planification.MatchDaySchedule.builder().build());
        }
        planification.getMatchDaySchedule().setDetailedSchedule(schedule);

        notificationService.sendNotification("Detailed schedule updated for planification " + planificationId);
        return planificationRepository.save(planification);
    }

    /**
     * Define team arrival times
     */
    public Planification defineTeamArrivals(String planificationId, List<Planification.TeamArrival> teamArrivals) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getMatchDaySchedule() == null) {
            planification.setMatchDaySchedule(Planification.MatchDaySchedule.builder().build());
        }
        planification.getMatchDaySchedule().setTeamArrivals(teamArrivals);

        return planificationRepository.save(planification);
    }

    /**
     * Plan technical meeting
     */
    public Planification planTechnicalMeeting(String planificationId, Planification.TechnicalMeeting technicalMeeting) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getMatchDaySchedule() == null) {
            planification.setMatchDaySchedule(Planification.MatchDaySchedule.builder().build());
        }
        planification.getMatchDaySchedule().setTechnicalMeeting(technicalMeeting);

        return planificationRepository.save(planification);
    }

    /**
     * Plan press conference
     */
    public Planification planPressConference(String planificationId, Planification.PressConference pressConference) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getMatchDaySchedule() == null) {
            planification.setMatchDaySchedule(Planification.MatchDaySchedule.builder().build());
        }
        planification.getMatchDaySchedule().setPressConference(pressConference);

        return planificationRepository.save(planification);
    }

    /**
     * Get planification history for a match
     */
    public Page<Planification> getPlanificationHistory(String matchId, Pageable pageable) {
        return planificationRepository.findByMatchId(matchId, pageable);
    }

    /**
     * Assess risk and security for a planification
     */
    public Planification assessRiskAndSecurity(String planificationId, String riskLevel, String securityNeeds, Integer estimatedAttendance) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getSecurityAndRevenue() == null) {
            planification.setSecurityAndRevenue(Planification.SecurityAndRevenue.builder().build());
        }

        planification.getSecurityAndRevenue().setRiskLevel(riskLevel);
        planification.getSecurityAndRevenue().setSecurityNeeds(securityNeeds);
        planification.getSecurityAndRevenue().setEstimatedAttendance(estimatedAttendance);

        return planificationRepository.save(planification);
    }

    /**
     * Estimate potential revenue for a planification
     */
    public Planification estimatePotentialRevenue(String planificationId, Double potentialRevenue) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getSecurityAndRevenue() == null) {
            planification.setSecurityAndRevenue(Planification.SecurityAndRevenue.builder().build());
        }

        planification.getSecurityAndRevenue().setPotentialRevenue(potentialRevenue);

        return planificationRepository.save(planification);
    }

    /**
     * Manage contingency planning
     */
    public Planification manageContingency(String planificationId, String contingencyPlan, String lastMinuteReport) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getContingencyInfo() == null) {
            planification.setContingencyInfo(Planification.ContingencyInfo.builder().build());
        }
        planification.getContingencyInfo().setContingencyPlan(contingencyPlan);
        planification.getContingencyInfo().setLastMinuteReport(lastMinuteReport);

        return planificationRepository.save(planification);
    }

    /**
     * Find and set a catch-up date for postponed match
     */
    public Planification findCatchUpDate(String planificationId, LocalDateTime catchUpDate, String reasonForChange) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getContingencyInfo() == null) {
            planification.setContingencyInfo(Planification.ContingencyInfo.builder().build());
        }
        if (planification.getAuditInfo() == null) {
            planification.setAuditInfo(Planification.AuditInfo.builder()
                    .historiqueModifications(new ArrayList<>())
                    .build());
        }

        planification.getContingencyInfo().setCatchUpDate(catchUpDate);
        planification.getAuditInfo().setReasonForChange(reasonForChange);
        planification.getAuditInfo().getHistoriqueModifications()
                .add("Catch-up date found: " + catchUpDate + " due to: " + reasonForChange);

        return planificationRepository.save(planification);
    }

    /**
     * Update planification protocols (entry, security, anti-doping, etc.)
     */
    public Planification updatePlanificationProtocols(String planificationId, String entryProtocol,
            String securityChecks, String antiDopingControl, String mixedZoneInterviews, String postMatchPressConference) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        if (planification.getMatchDaySchedule() == null) {
            planification.setMatchDaySchedule(Planification.MatchDaySchedule.builder().build());
        }

        if (entryProtocol != null) {
            planification.getMatchDaySchedule().setEntryProtocol(entryProtocol);
        }
        if (securityChecks != null) {
            planification.getMatchDaySchedule().setSecurityChecks(securityChecks);
        }
        if (antiDopingControl != null) {
            planification.getMatchDaySchedule().setAntiDopingControl(antiDopingControl);
        }
        if (mixedZoneInterviews != null) {
            planification.getMatchDaySchedule().setMixedZoneInterviews(mixedZoneInterviews);
        }
        if (postMatchPressConference != null) {
            planification.getMatchDaySchedule().setPostMatchPressConference(postMatchPressConference);
        }

        return planificationRepository.save(planification);
    }

    public Planification generateDateProposals(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        List<LocalDateTime> proposals = new ArrayList<>();
        proposals.add(planification.getDatePropose().plusDays(1));
        proposals.add(planification.getDatePropose().plusDays(2));
        planification.setAlternativeDates(proposals);
        notificationService.sendNotification("New date proposals generated for " + planificationId);
        return planificationRepository.save(planification);
    }

    /**
     * Validate a planning
     */
    public Planification validatePlanning(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found"));

        if (planification.getWorkflowInfo() == null) {
            planification.setWorkflowInfo(Planification.WorkflowInfo.builder().build());
        }

        planification.getWorkflowInfo().setValidated(true);
        notificationService.sendNotification("Planning " + planificationId + " has been validated");
        return planificationRepository.save(planification);
    }


    /**
     * Check if teams have minimum rest period
     */
    public String checkTeamRestPeriod(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found"));

        if (planification.getConstraints() == null) {
            return "Constraints not set";
        }

        // Simplified logic: assumes a fixed rest period of 3 days
        if (planification.getConstraints().getTeam1MinRestDays() < 3 ||
            planification.getConstraints().getTeam2MinRestDays() < 3) {
            return "One or both teams do not have the minimum rest period.";
        }
        return "Minimum rest period check passed";
    }

    /**
     * Check for calendar conflicts with other matches
     */
    public Planification checkCalendarConflicts(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found"));

        if (planification.getWorkflowInfo() == null) {
            planification.setWorkflowInfo(Planification.WorkflowInfo.builder().build());
        }

        // Simplified logic: checks for other matches on the same day
        Page<Match> otherMatches = matchRepository.findByDate(planification.getDatePropose(), Pageable.unpaged());
        if (otherMatches.getTotalElements() > 1) {
            planification.getWorkflowInfo().setCalendarConflict("Conflict: another match is scheduled on the same day.");
        } else {
            planification.getWorkflowInfo().setCalendarConflict("No conflicts found");
        }

        return planificationRepository.save(planification);
    }

    /**
     * Check TV broadcasting constraints
     */
    public Planification checkTvConstraints(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found"));

        if (planification.getWorkflowInfo() == null) {
            planification.setWorkflowInfo(Planification.WorkflowInfo.builder().build());
        }

        // Simplified logic: checks if the match is on a weekend
        if (planification.getDatePropose().getDayOfWeek().getValue() >= 6) {
            planification.getWorkflowInfo().setTvConstraint("No TV constraints");
        } else {
            planification.getWorkflowInfo().setTvConstraint("Potential TV constraint: match is on a weekday.");
        }

        return planificationRepository.save(planification);
    }

    public LocalDateTime proposeBestAvailableDate(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        List<LocalDateTime> possibleDates = new ArrayList<>();
        possibleDates.add(planification.getDatePropose());
        if (planification.getAlternativeDates() != null) {
            possibleDates.addAll(planification.getAlternativeDates());
        }

        LocalDateTime bestDate = null;
        int bestScore = -1;

        for (LocalDateTime date : possibleDates) {
            int score = 0;
            // Simplified scoring logic. A real implementation would be much more complex.
            if (isStadiumAvailable(date)) {
                score += 10;
            }
            if (isTvBlackout(date)) {
                score -= 5;
            }
            if (isInternationalBreak(date)) {
                score -= 10;
            }

            if (score > bestScore) {
                bestScore = score;
                bestDate = date;
            }
        }
        return bestDate;
    }

    private boolean isStadiumAvailable(LocalDateTime date) {
        // Placeholder
        return true;
    }

    private boolean isTvBlackout(LocalDateTime date) {
        // Placeholder
        return false;
    }

    private boolean isInternationalBreak(LocalDateTime date) {
        // Placeholder
        return false;
    }

    public List<LocalDateTime> suggestAlternatives(String planificationId) {
        // Placeholder for alternative suggestion logic
        List<LocalDateTime> alternatives = new ArrayList<>();
        alternatives.add(LocalDateTime.now().plusDays(10));
        alternatives.add(LocalDateTime.now().plusDays(12));
        return alternatives;
    }

    public Planification submitForValidation(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        planification.setStatut(PlanificationStatus.SUBMITTED_FOR_VALIDATION.name());
        notificationService.sendNotification("Planification " + planificationId + " submitted for validation");
        return planificationRepository.save(planification);
    }

    public Planification approvePlanification(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        planification.setStatut(PlanificationStatus.APPROVED.name());
        notificationService.sendNotification("Planification " + planificationId + " has been approved");
        return planificationRepository.save(planification);
    }

    /**
     * Reject a planification with reason
     */
    public Planification rejectPlanification(String planificationId, String reason) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found"));

        planification.setStatut(PlanificationStatus.REJECTED.name());

        if (planification.getAuditInfo() == null) {
            planification.setAuditInfo(Planification.AuditInfo.builder()
                    .historiqueModifications(new ArrayList<>())
                    .build());
        }
        planification.getAuditInfo().getHistoriqueModifications().add("Rejected due to: " + reason);

        notificationService.sendNotification("Planification " + planificationId + " has been rejected");
        return planificationRepository.save(planification);
    }

    /**
     * Request modification for a planification
     */
    public Planification requestModification(String planificationId, String reason) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found"));

        planification.setStatut(PlanificationStatus.MODIFICATION_REQUESTED.name());

        if (planification.getAuditInfo() == null) {
            planification.setAuditInfo(Planification.AuditInfo.builder()
                    .historiqueModifications(new ArrayList<>())
                    .build());
        }
        planification.getAuditInfo().getHistoriqueModifications().add("Modification requested due to: " + reason);

        notificationService.sendNotification("Modification requested for planification " + planificationId);
        return planificationRepository.save(planification);
    }

    public void notifyStakeholders(String planificationId, String message) {
        // In a real scenario, this would involve more complex logic to identify and notify all stakeholders
        notificationService.sendNotification("Attention Stakeholders for Planification " + planificationId + ": " + message);
    }

    /**
     * Plan warm-up sessions for teams
     */
    public Planification planWarmUps(String planificationId, List<Planification.WarmUp> warmUps) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found"));

        if (planification.getMatchDaySchedule() == null) {
            planification.setMatchDaySchedule(Planification.MatchDaySchedule.builder().build());
        }
        planification.getMatchDaySchedule().setWarmUps(warmUps);

        notificationService.sendNotification("Warm-ups planned for " + planificationId);
        return planificationRepository.save(planification);
    }

    /**
     * Lock planning when match date is close
     */
    public Planification lockPlanning(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        if (planification.getDatePropose().isBefore(LocalDateTime.now().plusDays(2))) {
            planification.setPlanningLocked(true);
            notificationService.sendNotification("Planning for " + planificationId + " is now locked");
        } else {
            notificationService.sendNotification("Planning for " + planificationId + " cannot be locked yet");
        }
        return planificationRepository.save(planification);
    }

    public Planification markAsConfirmed(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        planification.setStatut(PlanificationStatus.CONFIRMED.name());
        notificationService.sendNotification("Planification " + planificationId + " has been marked as confirmed");
        return planificationRepository.save(planification);
    }

    /**
     * Schedule matches for a championship using round-robin algorithm
     */
    public List<Match> scheduleMatches(List<String> teamIds) {
        List<String> teams = new java.util.ArrayList<>(teamIds);
        if (teams.size() % 2 != 0) {
            teams.add("BYE");
        }

        java.util.Collections.shuffle(teams);

        List<Match> scheduledMatches = new java.util.ArrayList<>();
        int numTeams = teams.size();
        int numRounds = numTeams - 1;

        for (int round = 0; round < numRounds; round++) {
            for (int i = 0; i < numTeams / 2; i++) {
                String team1 = teams.get(i);
                String team2 = teams.get(numTeams - 1 - i);

                if (!team1.equals("BYE") && !team2.equals("BYE")) {
                    Match match = new Match();
                    match.setTeam1Id(team1);
                    match.setTeam2Id(team2);
                    match.setDate(LocalDateTime.now().plusWeeks(round + 1));
                    scheduledMatches.add(match);
                }
            }

            // Rotate teams for the next round
            String lastTeam = teams.remove(numTeams - 1);
            teams.add(1, lastTeam);
        }

        return matchRepository.saveAll(scheduledMatches);
    }

    /**
     * Update match date/time for a planification
     */
    public Planification updateMatchDateTime(String planificationId, LocalDateTime newDateTime) {
        Planification planification = planificationRepository.findById(planificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));

        planification.setDatePropose(newDateTime);

        if (planification.getAuditInfo() == null) {
            planification.setAuditInfo(Planification.AuditInfo.builder()
                    .historiqueModifications(new ArrayList<>())
                    .build());
        }
        planification.getAuditInfo().getHistoriqueModifications().add("Match date/time updated to: " + newDateTime);

        notificationService.sendNotification("Match date/time for " + planificationId + " updated");
        return planificationRepository.save(planification);
    }
}
