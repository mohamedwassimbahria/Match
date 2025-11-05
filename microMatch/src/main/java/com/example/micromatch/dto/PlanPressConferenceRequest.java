package com.example.micromatch.dto;

import com.example.micromatch.entity.Planification;
import lombok.Data;

@Data
public class PlanPressConferenceRequest {
    private Planification.PressConference pressConference;
}
