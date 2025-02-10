package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for Seance conflicts.
 * Represents conflicts between two SeanceDTO objects.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeanceConflictDTO {
    private SeanceDTO seance1;  // First conflicting session
    private SeanceDTO seance2;  // Second conflicting session

    // List of conflict types between the two sessions
    private List<String> conflictTypes;
}
