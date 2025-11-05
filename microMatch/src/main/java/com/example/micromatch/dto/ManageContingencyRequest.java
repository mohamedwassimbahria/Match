package com.example.micromatch.dto;

import lombok.Data;

@Data
public class ManageContingencyRequest {
    private String contingencyPlan;
    private String lastMinuteReport;
}
