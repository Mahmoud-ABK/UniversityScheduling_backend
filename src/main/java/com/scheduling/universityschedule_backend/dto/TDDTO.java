package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class TDDTO {
    private Long id;
    private int nb;
    private int nbTP;
    // Represent associated Branche by its ID
    private Long brancheId;
    // List of associated TP IDs
    private List<Long> tpIds;
}
