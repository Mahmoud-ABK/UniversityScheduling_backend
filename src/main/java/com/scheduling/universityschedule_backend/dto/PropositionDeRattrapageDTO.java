package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class PropositionDeRattrapageDTO {
    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime date;

    @NotNull
    @Size(min = 5, max = 500)
    private String reason;

    @NotNull
    private String status;
}
