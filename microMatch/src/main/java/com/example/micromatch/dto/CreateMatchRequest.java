package com.example.micromatch.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateMatchRequest {
    private String team1Id;
    private String team2Id;
    private LocalDateTime date;
}
