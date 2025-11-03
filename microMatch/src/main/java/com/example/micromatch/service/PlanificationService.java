package com.example.micromatch.service;

import com.example.micromatch.entity.Planification;
import com.example.micromatch.enums.PlanificationStatus;
import com.example.micromatch.repository.PlanificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PlanificationService {

    private final PlanificationRepository planificationRepository;

    public Planification createPlanification(String matchId, LocalDateTime datePropose, java.util.List<String> contraintes) {
        Planification planification = Planification.builder()
                .matchId(matchId)
                .datePropose(datePropose)
                .statut(PlanificationStatus.PROPOSED.name())
                .contraintes(contraintes)
                .historiqueModifications(new ArrayList<>())
                .build();
        return planificationRepository.save(planification);
    }

    public Planification confirmPlanification(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new RuntimeException("Planification not found"));
        planification.setStatut(PlanificationStatus.CONFIRMED.name());
        return planificationRepository.save(planification);
    }

    public Planification cancelPlanification(String planificationId) {
        Planification planification = planificationRepository.findById(planificationId).orElseThrow(() -> new RuntimeException("Planification not found"));
        planification.setStatut(PlanificationStatus.CANCELLED.name());
        return planificationRepository.save(planification);
    }
}
