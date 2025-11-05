package com.example.micromatch.service;

import com.example.micromatch.entity.Planification;
import com.example.micromatch.repository.PlanificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PlanificationServiceTest {

    @InjectMocks
    private PlanificationService planificationService;

    @Mock
    private PlanificationRepository planificationRepository;

    @Mock
    private NotificationService notificationService;

    @Test
    public void testCreatePlanification() {
        when(planificationRepository.save(any(Planification.class))).thenReturn(new Planification());
        Planification createdPlanification = planificationService.createPlanification("match1", LocalDateTime.now());
        assertEquals(createdPlanification != null, true);
    }

    @Test
    void defineTeamArrivals() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        List<Planification.TeamArrival> arrivals = new ArrayList<>();
        arrivals.add(new Planification.TeamArrival("team1", LocalDateTime.now()));
        Planification updated = planificationService.defineTeamArrivals("1", arrivals);

        assertEquals(arrivals, updated.getTeamArrivals());
    }

    @Test
    void planTechnicalMeeting() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification.TechnicalMeeting meeting = new Planification.TechnicalMeeting(LocalDateTime.now(), "Conference Room");
        Planification updated = planificationService.planTechnicalMeeting("1", meeting);

        assertEquals(meeting, updated.getTechnicalMeeting());
    }

    @Test
    void planPressConference() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification.PressConference conference = new Planification.PressConference(LocalDateTime.now(), "Media Room");
        Planification updated = planificationService.planPressConference("1", conference);

        assertEquals(conference, updated.getPressConference());
    }

    @Test
    void assessRiskAndSecurity() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification updated = planificationService.assessRiskAndSecurity("1", "High", "Extra Police", 50000);

        assertEquals("High", updated.getRiskLevel());
        assertEquals("Extra Police", updated.getSecurityNeeds());
        assertEquals(50000, updated.getEstimatedAttendance());
    }

    @Test
    void estimatePotentialRevenue() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification updated = planificationService.estimatePotentialRevenue("1", 100000.0);

        assertEquals(100000.0, updated.getPotentialRevenue());
    }

    @Test
    void manageContingency() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification updated = planificationService.manageContingency("1", "Plan B", "Report details");

        assertEquals("Plan B", updated.getContingencyPlan());
        assertEquals("Report details", updated.getLastMinuteReport());
    }

    @Test
    void findCatchUpDate() {
        Planification planification = new Planification();
        planification.setId("1");
        planification.setHistoriqueModifications(new ArrayList<>());
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        LocalDateTime catchUpDate = LocalDateTime.now().plusDays(1);
        Planification updated = planificationService.findCatchUpDate("1", catchUpDate, "Heavy rain");

        assertEquals(catchUpDate, updated.getCatchUpDate());
        assertEquals("Heavy rain", updated.getReasonForChange());
    }

    @Test
    void setEntryProtocol() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification updated = planificationService.setEntryProtocol("1", "Protocol XYZ");

        assertEquals("Protocol XYZ", updated.getEntryProtocol());
    }

    @Test
    void setSecurityChecks() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification updated = planificationService.setSecurityChecks("1", "Standard checks");

        assertEquals("Standard checks", updated.getSecurityChecks());
    }

    @Test
    void setAntiDopingControl() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification updated = planificationService.setAntiDopingControl("1", "Random testing");

        assertEquals("Random testing", updated.getAntiDopingControl());
    }

    @Test
    void setMixedZoneInterviews() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification updated = planificationService.setMixedZoneInterviews("1", "Post-match interviews");

        assertEquals("Post-match interviews", updated.getMixedZoneInterviews());
    }

    @Test
    void setPostMatchPressConference() {
        Planification planification = new Planification();
        planification.setId("1");
        when(planificationRepository.findById("1")).thenReturn(Optional.of(planification));
        when(planificationRepository.save(any(Planification.class))).thenReturn(planification);

        Planification updated = planificationService.setPostMatchPressConference("1", "At 9 PM");

        assertEquals("At 9 PM", updated.getPostMatchPressConference());
    }
}
