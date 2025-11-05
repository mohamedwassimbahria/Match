package com.example.micromatch.dto;

import com.example.micromatch.entity.Planification;
import lombok.Data;
import java.util.List;

@Data
public class PlanWarmUpsRequest {
    private List<Planification.WarmUp> warmUps;
}
