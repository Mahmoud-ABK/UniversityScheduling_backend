package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EtudiantDTO extends PersonneDTO {
    private String matricule;
    // Representing the associated Branche by its ID
    private Long brancheId;
    // Representation of associated TP record by its ID
    private Long tpId;
}
