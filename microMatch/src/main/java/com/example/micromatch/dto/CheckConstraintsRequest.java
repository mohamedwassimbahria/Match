package com.example.micromatch.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckConstraintsRequest {
    private String matchId;
    private LocalDateTime date;
}
