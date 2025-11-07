package com.example.micromatch.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchMatchesRequest {
    private String teamId;
    private LocalDateTime date;
    private String status;
    private String stadium;
}
