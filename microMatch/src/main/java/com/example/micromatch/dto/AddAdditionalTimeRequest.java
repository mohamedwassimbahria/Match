package com.example.micromatch.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AddAdditionalTimeRequest {
    @Min(value = 1, message = "Additional time must be at least 1 minute")
    private int additionalTime;
}
