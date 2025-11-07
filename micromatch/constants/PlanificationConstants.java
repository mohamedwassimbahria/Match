package com.example.micromatch.constants;

/**
 * Constants for Planification operations and values
 */
public final class PlanificationConstants {

    private PlanificationConstants() {
        // Prevent instantiation
    }

    // Timing constants
    public static final int MIN_DAYS_BEFORE_LOCK = 2;
    public static final int TEAM_ARRIVAL_HOURS_BEFORE_MATCH = 2;
    public static final int WARMUP_DURATION_MINUTES = 30;
    public static final int TECHNICAL_MEETING_MINUTES_BEFORE_MATCH = 90;

    // Risk levels
    public static final String RISK_LEVEL_LOW = "LOW";
    public static final String RISK_LEVEL_MEDIUM = "MEDIUM";
    public static final String RISK_LEVEL_HIGH = "HIGH";
    public static final String RISK_LEVEL_CRITICAL = "CRITICAL";

    // Workflow actions
    public static final String ACTION_SUBMIT = "SUBMIT";
    public static final String ACTION_APPROVE = "APPROVE";
    public static final String ACTION_REJECT = "REJECT";
    public static final String ACTION_REQUEST_MODIFICATION = "REQUEST_MODIFICATION";
    public static final String ACTION_CONFIRM = "CONFIRM";
    public static final String ACTION_CANCEL = "CANCEL";
    public static final String ACTION_VALIDATE = "VALIDATE";
    public static final String ACTION_LOCK = "LOCK";

    // Notification messages
    public static final String MSG_PLANIFICATION_CREATED = "Planification created for match %s";
    public static final String MSG_PLANIFICATION_CONFIRMED = "Planification %s has been confirmed";
    public static final String MSG_PLANIFICATION_CANCELLED = "Planification %s has been cancelled";
    public static final String MSG_PLANIFICATION_APPROVED = "Planification %s has been approved";
    public static final String MSG_PLANIFICATION_REJECTED = "Planification %s has been rejected";
    public static final String MSG_PLANIFICATION_VALIDATED = "Planning %s has been validated";
    public static final String MSG_PLANIFICATION_LOCKED = "Planning for %s is now locked";
    public static final String MSG_CONSTRAINTS_CHECKED = "Constraints checked for planification %s";
    public static final String MSG_SCHEDULE_UPDATED = "Detailed schedule updated for planification %s";
}

