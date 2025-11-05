package com.example.micromatch.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssignAssistantRefereesRequest {
    private List<String> assistantReferees;
}
