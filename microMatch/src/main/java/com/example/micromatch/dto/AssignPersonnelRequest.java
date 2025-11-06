package com.example.micromatch.dto;

import com.example.micromatch.enums.PersonnelRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignPersonnelRequest {

    @NotNull(message = "Personnel role cannot be null")
    private PersonnelRole role;

    // Used for CAPTAIN
    private String teamId;
    private String playerId;

    // Used for MAIN_REFEREE, FOURTH_OFFICIAL
    private String name;

    // Used for ASSISTANT_REFEREE, VAR_REFEREE
    private List<String> names;
}
