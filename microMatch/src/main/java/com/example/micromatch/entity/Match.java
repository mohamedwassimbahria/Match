package com.example.micromatch.entity;

import com.example.micromatch.enums.MatchPhase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    private String team1Id;
    private String team2Id;

    private LocalDateTime date;
    private String status;
    private MatchPhase matchPhase;

    private Map<String, Integer> score;
    private List<Event> events;

    private String mainReferee;
    private List<String> assistantReferees;
    private String fourthOfficial;
    private List<String> varReferees;
    private String team1Coach;
    private String team2Coach;

    private List<String> team1Lineup;
    private List<String> team1Substitutes;
    private String team1TacticalSystem;
    private String team1CaptainId;

    private List<String> team2Lineup;
    private List<String> team2Substitutes;
    private String team2TacticalSystem;
    private String team2CaptainId;

    private Map<String, Map<String, Object>> collectiveStats; // teamId -> statName -> value
    private List<PlayerStats> individualStats;

    private List<Media> media;

    private Integer additionalTime;

    private List<ArbitralDecision> decisions;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer currentMinute;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerStats {
        private String playerId;
        private String teamId;
        private Map<String, Object> stats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Media {
        private String id;
        private String type; // "PHOTO", "VIDEO", "INTERVIEW"
        private String url;
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArbitralDecision {
        private String id;
        private int minute;
        private String decision;
        private String description;
    }

    public static final List<String> TUNISIAN_REFEREES = Arrays.asList(
            "Sadok Selmi",
            "Youssef Srairi",
            "Haythem Guirat",
            "Naim Hosni",
            "Oussama Rezgallah"
    );

    public static final List<String> TUNISIAN_COACHES = Arrays.asList(
            "Mondher Kebaier",
            "Faouzi Benzarti",
            "Nabil Maaloul",
            "Lassaad Dridi",
            "Sami Trabelsi"
    );
}
