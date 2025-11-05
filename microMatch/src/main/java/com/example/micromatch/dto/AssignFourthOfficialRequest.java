package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignFourthOfficialRequest {
    @NotBlank(message = "Fourth official cannot be blank")
    private String fourthOfficial;
}
