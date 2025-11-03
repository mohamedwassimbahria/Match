package com.example.micromatch.dto;

import lombok.Data;

@Data
public class UpdateScoreRequest {
    private String teamId;
    private int score;
}
