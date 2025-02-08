package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeanceConflictDTO {
    private SeanceDTO seance1;
    private SeanceDTO seance2;
    private List<String> conflictTypes; // A list to hold the conflict types
}
