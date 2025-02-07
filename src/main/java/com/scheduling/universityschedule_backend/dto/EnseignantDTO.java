package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class EnseignantDTO extends PersonneDTO {
    @NotNull
    private String codeEnseignant;

    private List<String> heures;

    private List<String> seances;

    private List<String> propositionsDeRattrapage;
}
