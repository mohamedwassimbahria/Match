package com.example.micromatch.dto;

import com.example.micromatch.entity.Planification;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class DefineTeamArrivalsRequest {
    @NotEmpty(message = "Team arrivals list cannot be empty")
    private List<Planification.TeamArrival> teamArrivals;
}
