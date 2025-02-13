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
    // ID of the first conflicting session
    private Long seance1Id;

    // ID of the second conflicting session
    private Long seance2Id;

    // List of conflict types between the two sessions
    private List<String> conflictTypes;
}
