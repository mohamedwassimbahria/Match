package com.example.micromatch.service;

import com.example.micromatch.entity.Planification;
import com.example.micromatch.enums.PlanificationStatus;
import com.example.micromatch.repository.PlanificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanificationServiceTest {

    @Mock
    private PlanificationRepository planificationRepository;

    @InjectMocks
    private PlanificationService planificationService;

    @Test
    void createPlanification() {
        Planification planification = new Planification();
        planification.setId("1");
        planification.setStatut(PlanificationStatus.PROPOSED.name());
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);
        Planification created = planificationService.createPlanification("1", LocalDateTime.now(), new ArrayList<>());
        assertEquals(PlanificationStatus.PROPOSED.name(), created.getStatut());
    }

    @Test
    void confirmPlanification() {
        Planification planification = new Planification();
        planification.setId("1");
        planification.setStatut(PlanificationStatus.PROPOSED.name());
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);
        Planification confirmed = planificationService.confirmPlanification("1");
        assertEquals(PlanificationStatus.CONFIRMED.name(), confirmed.getStatut());
    }
}
