package com.example.micromatch.dto;

import lombok.Data;

@Data
public class AssignCaptainRequest {
    private String teamId;
    private String playerId;
}
