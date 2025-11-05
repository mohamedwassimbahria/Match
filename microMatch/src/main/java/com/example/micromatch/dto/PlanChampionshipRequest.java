package com.example.micromatch.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlanChampionshipRequest {
    private List<String> teamIds;
}
