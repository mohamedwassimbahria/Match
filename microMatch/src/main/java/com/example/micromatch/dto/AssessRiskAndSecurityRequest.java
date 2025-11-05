package com.example.micromatch.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssessRiskAndSecurityRequest {
    @NotBlank(message = "Risk level cannot be blank")
    private String riskLevel;
    @NotBlank(message = "Security needs cannot be blank")
    private String securityNeeds;
    @NotNull(message = "Estimated attendance cannot be null")
    @Min(value = 0, message = "Estimated attendance cannot be negative")
    private Integer estimatedAttendance;
}
