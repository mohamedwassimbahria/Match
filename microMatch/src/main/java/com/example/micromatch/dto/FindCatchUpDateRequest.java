package com.example.micromatch.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FindCatchUpDateRequest {
    private LocalDateTime catchUpDate;
    private String reasonForChange;
}
