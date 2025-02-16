package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeanceRoomConflictDTO {
    private Long seance1Id;          // ID of the first conflicting session
    private Long seance2Id;          // ID of the second conflicting session
    private String conflictType;     // Type of conflict (e.g., "Room Conflict")
}