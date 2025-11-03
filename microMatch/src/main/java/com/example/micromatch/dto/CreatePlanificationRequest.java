package com.example.micromatch.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatePlanificationRequest {
    private String matchId;
    private LocalDateTime datePropose;
    private List<String> contraintes;
}
