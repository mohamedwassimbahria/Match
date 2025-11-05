package com.example.micromatch.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class PlanChampionshipRequest {
    @NotEmpty(message = "Team IDs list cannot be empty")
    private List<String> teamIds;
}
