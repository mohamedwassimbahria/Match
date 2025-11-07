package com.example.micromatch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "planifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planification {

    @Id
    private String id;

    // Core planification information
    private String matchId;
    private LocalDateTime datePropose;
    private String statut;
    private List<LocalDateTime> alternativeDates;

    // Embedded objects for better organization
    private PlanificationConstraints constraints;
    private SecurityAndRevenue securityAndRevenue;
    private WorkflowInfo workflowInfo;
    private AuditInfo auditInfo;
    private MatchDaySchedule matchDaySchedule;
    private ContingencyInfo contingencyInfo;

    /**
     * Embedded class for planification constraints
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlanificationConstraints {
        private boolean stadiumAvailable;
        private int team1MinRestDays;
        private int team2MinRestDays;
        private boolean tvBlackout;
        private boolean internationalBreak;
    }

    /**
     * Embedded class for security assessment and revenue estimation
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SecurityAndRevenue {
        private String riskLevel;
        private String securityNeeds;
        private Integer estimatedAttendance;
        private Double potentialRevenue;
    }

    /**
     * Embedded class for workflow and validation information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkflowInfo {
        private boolean validated;
        private boolean planningLocked;
        private String calendarConflict;
        private String tvConstraint;
    }

    /**
     * Embedded class for audit trail and change history
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuditInfo {
        private List<String> historiqueModifications;
        private String reasonForChange;
    }

    /**
     * Embedded class for match day schedule and protocols
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MatchDaySchedule {
        private Map<String, LocalDateTime> detailedSchedule; // e.g., "team1_arrival", "warmup_start"
        private List<TeamArrival> teamArrivals;
        private TechnicalMeeting technicalMeeting;
        private PressConference pressConference;
        private List<WarmUp> warmUps;
        private String entryProtocol;
        private String securityChecks;
        private String antiDopingControl;
        private String mixedZoneInterviews;
        private String postMatchPressConference;
    }

    /**
     * Embedded class for contingency planning
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ContingencyInfo {
        private String globalCalendarImpact;
        private String contingencyPlan;
        private String lastMinuteReport;
        private LocalDateTime catchUpDate;
    }

    /**
     * Team arrival information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamArrival {
        private String teamId;
        private LocalDateTime arrivalTime;
    }

    /**
     * Technical meeting details
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechnicalMeeting {
        private LocalDateTime time;
        private String location;
    }

    /**
     * Press conference details
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PressConference {
        private LocalDateTime time;
        private String location;
    }

    /**
     * Team warm-up schedule
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarmUp {
        private String teamId;
        private LocalDateTime startTime;
        private int durationInMinutes;
    }
}
