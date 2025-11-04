package com.example.micromatch.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class UpdateDetailedScheduleRequest {
    private Map<String, LocalDateTime> schedule;
}
