package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for FichierExcel entity.
 * Represents scheduling data imported into the system.
 */
@Data
public class FichierExcelDTO {
    private Long id;               // Unique identifier for the file
    private String fileName;       // Name of the Excel file
    private String status;         // Import status (e.g., successful, failed)

    // List of errors encountered during import
    private List<String> errors;

    private LocalDateTime importDate;  // Date and time of import
}
