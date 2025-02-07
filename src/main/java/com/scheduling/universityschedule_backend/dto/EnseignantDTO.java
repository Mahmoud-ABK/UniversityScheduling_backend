package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnseignantDTO extends PersonneDTO {
    private String codeEnseignant;
    private int heures;
    // Association fields represented by IDs
    private List<Long> seanceIds;
    private List<Long> propositionsDeRattrapageIds;
}
