package com.example.micromatch.constants;

/**
 * Constants for Match operations and values
 */
public final class MatchConstants {

    private MatchConstants() {
        // Prevent instantiation
    }

    // Match timing constants
    public static final int STANDARD_MATCH_DURATION_MINUTES = 90;
    public static final int HALF_TIME_DURATION_MINUTES = 15;
    public static final int MIN_REST_DAYS_BETWEEN_MATCHES = 3;
    public static final int MAX_ADDITIONAL_TIME_MINUTES = 15;

    // Team setup constants
    public static final int MAX_LINEUP_PLAYERS = 11;
    public static final int MAX_SUBSTITUTES = 12;
    public static final int MAX_SUBSTITUTIONS_PER_MATCH = 5;

    // Officials constants
    public static final int REQUIRED_ASSISTANT_REFEREES = 2;
    public static final int REQUIRED_VAR_REFEREES = 2;

    // Event types (complementing EventType enum)
    public static final String EVENT_GOAL = "GOAL";
    public static final String EVENT_YELLOW_CARD = "YELLOW_CARD";
    public static final String EVENT_RED_CARD = "RED_CARD";
    public static final String EVENT_SUBSTITUTION = "SUBSTITUTION";
    public static final String EVENT_PENALTY = "PENALTY";
    public static final String EVENT_VAR_REVIEW = "VAR_REVIEW";

    // Media types
    public static final String MEDIA_TYPE_PHOTO = "PHOTO";
    public static final String MEDIA_TYPE_VIDEO = "VIDEO";
    public static final String MEDIA_TYPE_INTERVIEW = "INTERVIEW";
    public static final String MEDIA_TYPE_HIGHLIGHT = "HIGHLIGHT";

    // Notification messages
    public static final String MSG_MATCH_CREATED = "Match created between %s and %s";
    public static final String MSG_MATCH_STARTED = "Match %s has started";
    public static final String MSG_MATCH_FINISHED = "Match %s has finished";
    public static final String MSG_MATCH_CANCELLED = "Match %s has been cancelled";
    public static final String MSG_MATCH_POSTPONED = "Match %s has been postponed to %s";
    public static final String MSG_GOAL_SCORED = "Goal scored in match %s by team %s";
    public static final String MSG_RED_CARD = "Red card in match %s";
}

