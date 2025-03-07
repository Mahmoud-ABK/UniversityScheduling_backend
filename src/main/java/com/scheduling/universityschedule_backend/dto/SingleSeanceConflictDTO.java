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
public class SingleSeanceConflictDTO {
    private Long seanceId;          // ID of the session
    private List<String> conflictTypes; // List of conflict types
}