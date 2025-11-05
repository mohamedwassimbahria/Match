package com.example.micromatch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EstimatePotentialRevenueRequest {
    @NotNull(message = "Potential revenue cannot be null")
    @Positive(message = "Potential revenue must be positive")
    private Double potentialRevenue;
}
