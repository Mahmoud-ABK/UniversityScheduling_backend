package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for TD entity.
 * Represents group tutorial sessions associated with a Branche.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TDDTO {
    private Long id;         // Unique identifier for the tutorial session
    private int nb;          // Number of tutorial sessions
    private int nbTP;        // Number of practical sessions associated

    // ID of the associated Branche representing the academic program or specialization
    private Long brancheId;

    // List of TP IDs representing practical sessions
    private List<Long> tpIds;

    private List<Long> seanceIds;
}
