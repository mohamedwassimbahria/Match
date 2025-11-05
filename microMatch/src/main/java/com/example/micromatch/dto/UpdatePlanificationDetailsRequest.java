package com.example.micromatch.dto;

import lombok.Data;

@Data
public class UpdatePlanificationDetailsRequest {
    private String entryProtocol;
    private String securityChecks;
    private String antiDopingControl;
    private String mixedZoneInterviews;
    private String postMatchPressConference;
}
