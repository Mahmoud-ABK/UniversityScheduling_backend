package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing Excel file imports
 */
public interface FichierExcelService {
    FichierExcelDTO importerFichierExcel(FichierExcelDTO fichierExcelDTO) throws CustomException;
}
