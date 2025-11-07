package com.example.micromatch.dto;

import com.example.micromatch.entity.Planification;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Generic DTO for all planification operations
 * Replaces: UpdatePlanificationRequest, DefineTeamArrivalsRequest, AssessRiskAndSecurityRequest,
 *           EstimatePotentialRevenueRequest, ManageContingencyRequest, FindCatchUpDateRequest,
 *           UpdateDetailedScheduleRequest, CheckConstraintsRequest, ReasonRequest
 */
@Data
public class PlanificationOperationRequest {

    @NotBlank(message = "Operation type is required")
    private String operation; // "UPDATE_SCHEDULE", "SET_ARRIVALS", "ASSESS_RISK", "SET_REVENUE", etc.

    // For schedule operations
    private Map<String, LocalDateTime> schedule;
    private List<Planification.TeamArrival> teamArrivals;
    private Planification.TechnicalMeeting technicalMeeting;
    private Planification.PressConference pressConference;
    private List<Planification.WarmUp> warmUps;

    // For protocols
    private String entryProtocol;
    private String securityChecks;
    private String antiDopingControl;
    private String mixedZoneInterviews;
    private String postMatchPressConference;

    // For security and revenue
    private String riskLevel;
    private String securityNeeds;
    private Integer estimatedAttendance;
    private Double potentialRevenue;

    // For contingency
    private String contingencyPlan;
    private String lastMinuteReport;
    private LocalDateTime catchUpDate;

    // For workflow
    private String reason;
    private String message;

    // For date/time updates
    private LocalDateTime newDateTime;
    private LocalDateTime date;

    // Constants for operation types
    public static final String OP_UPDATE_SCHEDULE = "UPDATE_SCHEDULE";
    public static final String OP_SET_ARRIVALS = "SET_ARRIVALS";
    public static final String OP_SET_TECHNICAL_MEETING = "SET_TECHNICAL_MEETING";
    public static final String OP_SET_PRESS_CONFERENCE = "SET_PRESS_CONFERENCE";
    public static final String OP_SET_WARMUPS = "SET_WARMUPS";
    public static final String OP_UPDATE_PROTOCOLS = "UPDATE_PROTOCOLS";
    public static final String OP_ASSESS_RISK = "ASSESS_RISK";
    public static final String OP_SET_REVENUE = "SET_REVENUE";
    public static final String OP_SET_CONTINGENCY = "SET_CONTINGENCY";
    public static final String OP_SET_CATCHUP_DATE = "SET_CATCHUP_DATE";
    public static final String OP_UPDATE_DATETIME = "UPDATE_DATETIME";
    public static final String OP_CHECK_CONSTRAINTS = "CHECK_CONSTRAINTS";
}

