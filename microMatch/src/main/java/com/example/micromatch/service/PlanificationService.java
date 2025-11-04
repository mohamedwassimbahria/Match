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
}
