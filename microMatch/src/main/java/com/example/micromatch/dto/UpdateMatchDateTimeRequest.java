package com.example.micromatch.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateMatchDateTimeRequest {
    private LocalDateTime newDateTime;
}
