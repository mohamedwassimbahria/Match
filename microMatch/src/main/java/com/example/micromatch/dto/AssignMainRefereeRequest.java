package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignMainRefereeRequest {
    @NotBlank(message = "Referee name cannot be blank")
    private String refereeName;
}
