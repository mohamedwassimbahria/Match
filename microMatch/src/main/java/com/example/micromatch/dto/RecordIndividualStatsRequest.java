package com.example.micromatch.dto;

import com.example.micromatch.entity.Match;
import lombok.Data;

import java.util.List;

@Data
public class RecordIndividualStatsRequest {
    private List<Match.PlayerStats> playerStats;
}
