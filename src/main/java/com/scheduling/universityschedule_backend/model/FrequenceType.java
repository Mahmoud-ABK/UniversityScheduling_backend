package com.scheduling.universityschedule_backend.model;

/**
 * Enum representing the frequency type of a session (Seance).
 * Used to indicate whether a session occurs weekly, biweekly, or is a makeup session.
 */
public enum FrequenceType {
    /**
     * Session occurs every week
     */
    WEEKLY,

    /**
     * Session occurs every other week (once per two weeks)
     */
    BIWEEKLY,

    /**
     * Session is a one-time makeup session
     */
    CATCHUP;

    /**
     * Convert a string representation to FrequenceType enum
     * @param value String value to convert
     * @return Corresponding FrequenceType or null if no match
     */
    public static FrequenceType fromString(String value) {
        if (value == null) {
            return null;
        }

        return switch (value.toLowerCase()) {
            case "weekly" -> WEEKLY;
            case "1/1" -> WEEKLY;
            case "biweekly" -> BIWEEKLY;
            case "1/15" -> BIWEEKLY;
            case "makeup", "catch-up"  -> CATCHUP;
            default -> null;
        };
    }

    /**
     * Convert FrequenceType to its string representation
     * @return String representation of frequency type
     */
    @Override
    public String toString() {
        return switch (this) {
            case WEEKLY -> "weekly";
            case BIWEEKLY -> "1/15";
            case CATCHUP -> "catch-up";
        };
    }
}