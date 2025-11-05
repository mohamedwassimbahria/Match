package com.example.micromatch.dto;

import lombok.Data;

@Data
public class AssessRiskAndSecurityRequest {
    private String riskLevel;
    private String securityNeeds;
    private Integer estimatedAttendance;
}
