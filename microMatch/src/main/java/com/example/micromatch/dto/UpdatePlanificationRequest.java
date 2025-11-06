package com.example.micromatch.dto;

import com.example.micromatch.entity.Planification;
import com.example.micromatch.enums.PlanificationUpdateType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePlanificationRequest {

    @NotNull
    private PlanificationUpdateType updateType;

    // Used for PRESS_CONFERENCE update type
    private Planification.PressConference pressConference;

    // Used for TECHNICAL_MEETING update type
    private Planification.TechnicalMeeting technicalMeeting;

    // Used for WARM_UPS update type
    private List<Planification.WarmUp> warmUps;

    // Used for DETAILS update type
    private String entryProtocol;
    private String securityChecks;
    private String antiDopingControl;
    private String mixedZoneInterviews;
    private String postMatchPressConference;

    // Used for UPDATE_DATETIME update type
    private java.time.LocalDateTime newDateTime;
}
