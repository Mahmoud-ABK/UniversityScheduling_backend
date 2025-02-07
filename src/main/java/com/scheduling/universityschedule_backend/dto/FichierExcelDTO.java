package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FichierExcelDTO {
    private Long id;
    private String fileName;
    private String status;
    private List<String> errors;
    private LocalDateTime importDate;
}
