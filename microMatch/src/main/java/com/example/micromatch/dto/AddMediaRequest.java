package com.example.micromatch.dto;

import com.example.micromatch.entity.Match;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddMediaRequest {
    @NotNull(message = "Media cannot be null")
    private Match.Media media;
}
