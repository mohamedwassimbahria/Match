package com.example.micromatch.entity;

import com.example.micromatch.enums.MatchPhase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    private String id;

    // Core match information
    private String team1Id;
    private String team2Id;
    private LocalDateTime date;
    private String status;
    private MatchPhase matchPhase;

    // Score and events
    private Map<String, Integer> score;
    private List<Event> events;

    // Embedded objects for better organization
    private TeamSetup team1Setup;
    private TeamSetup team2Setup;
    private MatchOfficials officials;
    private MatchTiming timing;

    // Statistics
    private Map<String, Map<String, Object>> collectiveStats; // teamId -> statName -> value
    private List<PlayerStats> individualStats;

    // Media and decisions
    private List<Media> media;
    private List<ArbitralDecision> decisions;

    /**
     * Embedded class for team setup information (lineup, substitutes, tactical system, captain, coach)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TeamSetup {
        private List<String> lineup;
        private List<String> substitutes;
        private String tacticalSystem;
        private String captainId;
        private String coach;
    }

    /**
     * Embedded class for match officials (referees and VAR)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MatchOfficials {
        private String mainReferee;
        private List<String> assistantReferees;
        private String fourthOfficial;
        private List<String> varReferees;
    }

    /**
     * Embedded class for match timing information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MatchTiming {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer currentMinute;
        private Integer additionalTime;
    }

    /**
     * Match event (goal, card, substitution, etc.)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Event {
        private String id;
        private int minute;
        private String type;
        private String teamId;
        private String playerId;
        private String description;
    }

    /**
     * Player statistics for a match
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerStats {
        private String playerId;
        private String teamId;
        private Map<String, Object> stats;
    }

    /**
     * Media content (photos, videos, interviews)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Media {
        private String id;
        private String type; // "PHOTO", "VIDEO", "INTERVIEW"
        private String url;
        private String title;
    }

    /**
     * Arbitral decision (VAR review, penalty decision, etc.)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArbitralDecision {
        private String id;
        private int minute;
        private String decision;
        private String description;
    }
}
