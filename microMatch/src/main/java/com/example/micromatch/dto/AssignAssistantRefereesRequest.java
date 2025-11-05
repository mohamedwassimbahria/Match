package com.example.micromatch.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AssignAssistantRefereesRequest {
    @NotEmpty(message = "Assistant referees list cannot be empty")
    private List<String> assistantReferees;
}
