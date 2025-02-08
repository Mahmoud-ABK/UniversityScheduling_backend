package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TDDTO {
    private Long id;
    private int nb;
    private int nbTP;

    private BrancheDTO branche; // Branche is assumed to be a DTO
    private List<TPDTO> tpList;
}
