package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class EtudiantDTO extends PersonneDTO {
    @NotNull
    private String matricule;

    @NotNull
    private String branche;

    private List<String> tp;
}
