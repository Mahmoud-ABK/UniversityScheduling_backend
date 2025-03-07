package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeanceConflictDTO {
    private Long seance1Id;          // ID of the first conflicting session
    private Long seance2Id;          // ID of the second conflicting session
    private List<String> conflictTypes; // List of conflict types
}