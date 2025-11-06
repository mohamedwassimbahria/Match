package com.example.micromatch.service;

import com.example.micromatch.entity.Planification;
import com.example.micromatch.enums.PlanificationStatus;
import com.example.micromatch.exception.ResourceNotFoundException;
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
    private final com.example.micromatch.repository.MatchRepository matchRepository;

    public Planification createPlanification(String matchId, LocalDateTime datePropose) {
        Planification planification = Planification.builder()
                .matchId(matchId)
                .datePropose(datePropose)
                .statut(PlanificationStatus.PROPOSED.name())
                .historiqueModifications(new ArrayList<>())
                .detailedSchedule(new HashMap<>())
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

    public Planification checkAllConstraints(String planificationId, LocalDateTime date) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        // Placeholder for constraint checking logic
        planification.setStadiumAvailable(true);
        planification.setTeam1MinRestDays(3);
        planification.setTeam2MinRestDays(3);
        planification.setTvBlackout(false);
        planification.setInternationalBreak(false);
        notificationService.sendNotification("Constraints checked for planification " + planificationId);
        return planificationRepository.save(planification);
    }

    public Planification updateDetailedSchedule(String planificationId, Map<String, LocalDateTime> schedule) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setDetailedSchedule(schedule);
        notificationService.sendNotification("Detailed schedule updated for planification " + planificationId);
        return planificationRepository.save(planification);
    }

    public Planification defineTeamArrivals(String planificationId, List<Planification.TeamArrival> teamArrivals) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setTeamArrivals(teamArrivals);
        return planificationRepository.save(planification);
    }

    public Planification planTechnicalMeeting(String planificationId, Planification.TechnicalMeeting technicalMeeting) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setTechnicalMeeting(technicalMeeting);
        return planificationRepository.save(planification);
    }

    public Planification planPressConference(String planificationId, Planification.PressConference pressConference) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setPressConference(pressConference);
        return planificationRepository.save(planification);
    }

    public String generateFullDaySchedule(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        StringBuilder schedule = new StringBuilder();
        schedule.append("Match Day Schedule for Match ID: ").append(planification.getMatchId()).append("\n");

        if (planification.getTeamArrivals() != null) {
            planification.getTeamArrivals().forEach(arrival -> {
                schedule.append("Team ").append(arrival.getTeamId()).append(" Arrival: ").append(arrival.getArrivalTime()).append("\n");
            });
        }
        if (planification.getTechnicalMeeting() != null) {
            schedule.append("Technical Meeting: ").append(planification.getTechnicalMeeting().getTime()).append(" at ").append(planification.getTechnicalMeeting().getLocation()).append("\n");
        }
        if (planification.getDetailedSchedule() != null) {
            planification.getDetailedSchedule().forEach((key, value) -> {
                schedule.append(key).append(": ").append(value).append("\n");
            });
        }
        if (planification.getPressConference() != null) {
            schedule.append("Press Conference: ").append(planification.getPressConference().getTime()).append(" at ").append(planification.getPressConference().getLocation()).append("\n");
        }
        return schedule.toString();
    }

    public Page<Planification> getPlanificationHistory(String matchId, Pageable pageable) {
        return planificationRepository.findByMatchId(matchId, pageable);
    }

    public String generatePlanificationReport(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        StringBuilder report = new StringBuilder();
        report.append("Planification Report for Match ID: ").append(planification.getMatchId()).append("\n");
        report.append("Status: ").append(planification.getStatut()).append("\n");
        report.append("Proposed Date: ").append(planification.getDatePropose()).append("\n");
        report.append("Risk Level: ").append(planification.getRiskLevel()).append("\n");
        report.append("Security Needs: ").append(planification.getSecurityNeeds()).append("\n");
        report.append("Estimated Attendance: ").append(planification.getEstimatedAttendance()).append("\n");
        report.append("Potential Revenue: ").append(planification.getPotentialRevenue()).append("\n");
        return report.toString();
    }

    public String exportPlanificationData(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        // Using a simple JSON representation for export
        return "{" +
                "\"id\":\"" + planification.getId() + "\"," +
                "\"matchId\":\"" + planification.getMatchId() + "\"," +
                "\"datePropose\":\"" + planification.getDatePropose() + "\"," +
                "\"statut\":\"" + planification.getStatut() + "\"" +
                "}";
    }

    public Planification assessRiskAndSecurity(String planificationId, String riskLevel, String securityNeeds, Integer estimatedAttendance) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setRiskLevel(riskLevel);
        planification.setSecurityNeeds(securityNeeds);
        planification.setEstimatedAttendance(estimatedAttendance);
        return planificationRepository.save(planification);
    }

    public Planification estimatePotentialRevenue(String planificationId, Double potentialRevenue) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setPotentialRevenue(potentialRevenue);
        return planificationRepository.save(planification);
    }

    public Planification manageContingency(String planificationId, String contingencyPlan, String lastMinuteReport) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setContingencyPlan(contingencyPlan);
        planification.setLastMinuteReport(lastMinuteReport);
        return planificationRepository.save(planification);
    }

    public Planification findCatchUpDate(String planificationId, LocalDateTime catchUpDate, String reasonForChange) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        planification.setCatchUpDate(catchUpDate);
        planification.setReasonForChange(reasonForChange);
        planification.getHistoriqueModifications().add("Catch-up date found: " + catchUpDate + " due to: " + reasonForChange);
        return planificationRepository.save(planification);
    }

    public Planification updatePlanificationDetails(String planificationId, com.example.micromatch.dto.UpdatePlanificationRequest request) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found with id " + planificationId));
        if (request.getEntryProtocol() != null) {
            planification.setEntryProtocol(request.getEntryProtocol());
        }
        if (request.getSecurityChecks() != null) {
            planification.setSecurityChecks(request.getSecurityChecks());
        }
        if (request.getAntiDopingControl() != null) {
            planification.setAntiDopingControl(request.getAntiDopingControl());
        }
        if (request.getMixedZoneInterviews() != null) {
            planification.setMixedZoneInterviews(request.getMixedZoneInterviews());
        }
        if (request.getPostMatchPressConference() != null) {
            planification.setPostMatchPressConference(request.getPostMatchPressConference());
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

    public Planification validatePlanning(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        planification.setValidated(true);
        notificationService.sendNotification("Planning " + planificationId + " has been validated");
        return planificationRepository.save(planification);
    }

    public Planification updateMatchDateTime(String planificationId, LocalDateTime newDateTime) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        planification.setDatePropose(newDateTime);
        planification.getHistoriqueModifications().add("Match date/time updated to: " + newDateTime);
        notificationService.sendNotification("Match date/time for " + planificationId + " updated");
        return planificationRepository.save(planification);
    }

    public String checkTeamRestPeriod(String planificationId) {
        // Simplified logic: assumes a fixed rest period of 3 days.
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        // A real implementation would need to check the last match date for both teams.
        if (planification.getTeam1MinRestDays() < 3 || planification.getTeam2MinRestDays() < 3) {
            return "One or both teams do not have the minimum rest period.";
        }
        return "Minimum rest period check passed";
    }

    public Planification checkCalendarConflicts(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        // Simplified logic: checks for other matches on the same day.
        Page<Match> otherMatches = matchRepository.findByDate(planification.getDatePropose(), Pageable.unpaged());
        if (otherMatches.getTotalElements() > 1) { // 1 being the current match
            planification.setCalendarConflict("Conflict: another match is scheduled on the same day.");
        } else {
            planification.setCalendarConflict("No conflicts found");
        }
        return planificationRepository.save(planification);
    }

    public Planification checkTvConstraints(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        // Simplified logic: checks if the match is on a weekend.
        if (planification.getDatePropose().getDayOfWeek().getValue() >= 6) { // Saturday or Sunday
            planification.setTvConstraint("No TV constraints");
        } else {
            planification.setTvConstraint("Potential TV constraint: match is on a weekday.");
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

    public Planification rejectPlanification(String planificationId, String reason) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        planification.setStatut(PlanificationStatus.REJECTED.name());
        planification.getHistoriqueModifications().add("Rejected due to: " + reason);
        notificationService.sendNotification("Planification " + planificationId + " has been rejected");
        return planificationRepository.save(planification);
    }

    public Planification requestModification(String planificationId, String reason) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        planification.setStatut(PlanificationStatus.MODIFICATION_REQUESTED.name());
        planification.getHistoriqueModifications().add("Modification requested due to: " + reason);
        notificationService.sendNotification("Modification requested for planification " + planificationId);
        return planificationRepository.save(planification);
    }

    public void notifyStakeholders(String planificationId, String message) {
        // In a real scenario, this would involve more complex logic to identify and notify all stakeholders
        notificationService.sendNotification("Attention Stakeholders for Planification " + planificationId + ": " + message);
    }

    public Planification planWarmUps(String planificationId, List<Planification.WarmUp> warmUps) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new ResourceNotFoundException("Planification not found"));
        planification.setWarmUps(warmUps);
        notificationService.sendNotification("Warm-ups planned for " + planificationId);
        return planificationRepository.save(planification);
    }

    public String analyzeGlobalCalendarImpact(String planificationId) {
        // Placeholder for impact analysis
        return "Global calendar impact analysis: Minimal";
    }

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

    public String planChampionship(List<String> teamIds) {
        if (teamIds.size() % 2 != 0) {
            teamIds.add("BYE"); // Add a bye week if there are an odd number of teams
        }

        int numTeams = teamIds.size();
        int numRounds = numTeams - 1;
        int matchesPerRound = numTeams / 2;
        StringBuilder schedule = new StringBuilder();

        for (int round = 0; round < numRounds; round++) {
            schedule.append("Round ").append(round + 1).append(":\n");
            for (int match = 0; match < matchesPerRound; match++) {
                String home = teamIds.get(match);
                String away = teamIds.get(numTeams - 1 - match);
                if (!home.equals("BYE") && !away.equals("BYE")) {
                    schedule.append("  ").append(home).append(" vs ").append(away).append("\n");
                }
            }
            // Rotate teams
            String lastTeam = teamIds.remove(numTeams - 1);
            teamIds.add(1, lastTeam);
        }

        return schedule.toString();
    }
}
