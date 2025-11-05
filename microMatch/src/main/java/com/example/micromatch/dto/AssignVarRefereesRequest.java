package com.example.micromatch.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AssignVarRefereesRequest {
    @NotEmpty(message = "VAR referees list cannot be empty")
    private List<String> varReferees;
}
