package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for Excel file management.
 * Handles upload and tracking of Excel schedule imports.
 */
public interface ExcelFileService {
    /**
     * Uploads and processes Excel file.
     * @param file Excel file DTO
     * @throws CustomException if upload fails
     */
    void upload(FichierExcelDTO file) throws CustomException;

    /**
     * Retrieves import history.
     * @return List of imported files
     * @throws CustomException if retrieval fails
     */
    List<FichierExcelDTO> getImportHistory() throws CustomException;
}
