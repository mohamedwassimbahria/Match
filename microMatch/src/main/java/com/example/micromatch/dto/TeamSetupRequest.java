package com.example.micromatch.dto;

import com.example.micromatch.enums.SetupType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TeamSetupRequest {

    @NotNull
    private SetupType setupType;

    private String teamId;

    // Used for LINEUP and SUBSTITUTES
    private List<String> playerIds;

    // Used for TACTICAL_SYSTEM
    private String tacticalSystem;
}
