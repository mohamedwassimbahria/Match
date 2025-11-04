package com.example.micromatch.service;

import com.example.micromatch.entity.Planification;
import com.example.micromatch.enums.PlanificationStatus;
import com.example.micromatch.repository.PlanificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanificationServiceTest {

    @Mock
    private PlanificationRepository planificationRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PlanificationService planificationService;

    @Test
    void createPlanification() {
        Planification planification = new Planification();
        planification.setId("1");
        planification.setStatut(PlanificationStatus.PROPOSED.name());
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);
        Planification created = planificationService.createPlanification("1", LocalDateTime.now());
        assertEquals(PlanificationStatus.PROPOSED.name(), created.getStatut());
        verify(notificationService).sendNotification("Planification created for match 1");
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
        verify(notificationService).sendNotification("Planification 1 has been confirmed");
    }

    @Test
    void checkAllConstraints() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);
        Planification checked = planificationService.checkAllConstraints("1", LocalDateTime.now());
        assertTrue(checked.isStadiumAvailable());
        verify(notificationService).sendNotification("Constraints checked for planification 1");
    }

    @Test
    void updateDetailedSchedule() {
        Planification planification = new Planification();
        planification.setId("1");
        planification.setDetailedSchedule(new HashMap<>());
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Map<String, LocalDateTime> schedule = new HashMap<>();
        schedule.put("team_arrival", LocalDateTime.now());
        Planification updated = planificationService.updateDetailedSchedule("1", schedule);

        assertEquals(schedule, updated.getDetailedSchedule());
        verify(notificationService).sendNotification("Detailed schedule updated for planification 1");
    }
}
