package com.example.micromatch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "planifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planification {

    @Id
    private String id;
    private String matchId;
    private LocalDateTime datePropose;
    private String statut;
    private List<String> historiqueModifications;

    // Advanced Constraints
    private boolean stadiumAvailable;
    private int team1MinRestDays;
    private int team2MinRestDays;
    private boolean tvBlackout;
    private boolean internationalBreak;

    // Day-of-Match Schedule
    private Map<String, LocalDateTime> detailedSchedule; // e.g., "team1_arrival", "warmup_start"
}
