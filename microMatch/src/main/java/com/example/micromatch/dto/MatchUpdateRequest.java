package com.example.micromatch.dto;

import com.example.micromatch.entity.Match;
import com.example.micromatch.enums.MatchUpdateType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MatchUpdateRequest {

    @NotNull
    private MatchUpdateType updateType;

    @Min(value = 1, message = "Additional time must be at least 1 minute")
    private Integer additionalTime;

    @Min(value = 0, message = "Minute must be non-negative")
    private Integer minute;

    @Min(value = 0, message = "Score cannot be negative")
    private Integer scoreTeam1;

    @Min(value = 0, message = "Score cannot be negative")
    private Integer scoreTeam2;

    private String eventId;

    private Match.Event event;

    private LocalDateTime newDateTime;
}
