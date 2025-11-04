package com.example.micromatch.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RecordCollectiveStatsRequest {
    private String teamId;
    private Map<String, Object> stats;
}
