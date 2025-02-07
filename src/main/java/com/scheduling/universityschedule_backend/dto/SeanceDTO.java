package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class SeanceDTO {
    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime jour;

    @NotNull
    private LocalDateTime heureDebut;

    @NotNull
    private LocalDateTime heureFin;

    @NotNull
    @Size(min = 3, max = 100)
    private String type;

    @NotNull
    @Size(min = 3, max = 100)
    private String matiere;

    @NotNull
    @Size(min = 1, max = 100)
    private String salle;

    @NotNull
    private String enseignant;
    @NotNull
    private String frequence;
}
