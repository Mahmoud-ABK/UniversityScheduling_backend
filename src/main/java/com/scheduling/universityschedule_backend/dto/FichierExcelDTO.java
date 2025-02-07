package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class FichierExcelDTO {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String fileName;

    private String status;

    private String errors;

    private LocalDateTime importDate;
}
