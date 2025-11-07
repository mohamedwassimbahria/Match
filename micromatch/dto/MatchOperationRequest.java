package com.example.micromatch.dto;

import com.example.micromatch.entity.Match;
import com.example.micromatch.enums.MatchPhase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Generic DTO for all match operations
 * Replaces: MatchUpdateRequest, TeamSetupRequest, AssignPersonnelRequest, 
 *           AddMediaRequest, AddArbitralDecisionRequest
 */
@Data
public class MatchOperationRequest {

    @NotBlank(message = "Operation type is required")
    private String operation; // "UPDATE_SCORE", "UPDATE_MINUTE", "ADD_TIME", "SET_LINEUP", "SET_SUBSTITUTES", etc.

    // For team operations
    private String teamId;
    private List<String> playerIds;
    private String tacticalSystem;
    private String captainId;

    // For timing operations
    @Min(value = 0, message = "Minute must be non-negative")
    private Integer minute;
    
    @Min(value = 1, message = "Additional time must be at least 1 minute")
    private Integer additionalTime;

    // For score operations
    @Min(value = 0, message = "Score cannot be negative")
    private Integer scoreTeam1;
    
    @Min(value = 0, message = "Score cannot be negative")
    private Integer scoreTeam2;

    // For event operations
    private String eventId;
    private Match.Event event;

    // For officials operations
    private String officialName;
    private List<String> officialNames;

    // For media operations
    private Match.Media media;

    // For arbitral decisions
    private Match.ArbitralDecision decision;

    // For stats operations
    private Map<String, Object> stats;
    private List<Match.PlayerStats> playerStats;

    // For date/time updates
    private LocalDateTime newDateTime;

    // For phase changes
    private MatchPhase newPhase;

    // Constants for operation types
    public static final String OP_UPDATE_SCORE = "UPDATE_SCORE";
    public static final String OP_UPDATE_MINUTE = "UPDATE_MINUTE";
    public static final String OP_ADD_TIME = "ADD_TIME";
    public static final String OP_UPDATE_EVENT = "UPDATE_EVENT";
    public static final String OP_UPDATE_DATETIME = "UPDATE_DATETIME";
    public static final String OP_SET_LINEUP = "SET_LINEUP";
    public static final String OP_SET_SUBSTITUTES = "SET_SUBSTITUTES";
    public static final String OP_SET_TACTICAL = "SET_TACTICAL";
    public static final String OP_SET_CAPTAIN = "SET_CAPTAIN";
    public static final String OP_SET_MAIN_REFEREE = "SET_MAIN_REFEREE";
    public static final String OP_SET_ASSISTANT_REFEREES = "SET_ASSISTANT_REFEREES";
    public static final String OP_SET_FOURTH_OFFICIAL = "SET_FOURTH_OFFICIAL";
    public static final String OP_SET_VAR_REFEREES = "SET_VAR_REFEREES";
}

