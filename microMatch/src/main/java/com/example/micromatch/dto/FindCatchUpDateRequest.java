package com.example.micromatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FindCatchUpDateRequest {
    @NotNull(message = "Catch-up date cannot be null")
    private LocalDateTime catchUpDate;
    @NotBlank(message = "Reason for change cannot be blank")
    private String reasonForChange;
}
