package com.example.micromatch.dto;

import com.example.micromatch.entity.Planification;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanTechnicalMeetingRequest {
    @NotNull(message = "Technical meeting cannot be null")
    private Planification.TechnicalMeeting technicalMeeting;
}
