package com.example.micromatch.service;

import com.example.micromatch.entity.Planification;
import com.example.micromatch.enums.PlanificationStatus;
import com.example.micromatch.exception.ResourceNotFoundException;
import com.example.micromatch.repository.PlanificationRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Planification> getPlanificationHistory(String matchId) {
        return planificationRepository.findByMatchId(matchId);
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
}
