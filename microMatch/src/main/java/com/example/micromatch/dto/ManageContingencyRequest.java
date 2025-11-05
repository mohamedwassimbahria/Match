package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManageContingencyRequest {
    @NotBlank(message = "Contingency plan cannot be blank")
    private String contingencyPlan;
    @NotBlank(message = "Last minute report cannot be blank")
    private String lastMinuteReport;
}
