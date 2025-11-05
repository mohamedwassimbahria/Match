package com.example.micromatch.dto;

import com.example.micromatch.entity.Planification;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanPressConferenceRequest {
    @NotNull(message = "Press conference cannot be null")
    private Planification.PressConference pressConference;
}
