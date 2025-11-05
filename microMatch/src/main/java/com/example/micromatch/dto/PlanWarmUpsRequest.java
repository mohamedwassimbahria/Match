package com.example.micromatch.dto;

import com.example.micromatch.entity.Planification;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class PlanWarmUpsRequest {
    @NotEmpty(message = "Warm-ups list cannot be empty")
    private List<Planification.WarmUp> warmUps;
}
