package com.example.micromatch.dto;

import lombok.Data;

import java.util.List;

@Data
public class DefineLineupRequest {
    private String teamId;
    private List<String> playerIds;
}
