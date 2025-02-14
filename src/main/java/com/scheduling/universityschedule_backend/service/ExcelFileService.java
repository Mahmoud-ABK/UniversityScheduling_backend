package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for Excel file management.
 * Handles upload and tracking of Excel schedule imports.
 */
public interface ExcelFileService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves an Excel file by ID.
     * @param id Excel file's unique identifier
     * @return Excel file DTO
     * @throws CustomException if file not found
     */
    FichierExcelDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all Excel files.
     * @return List of all Excel file imports
     * @throws CustomException if retrieval fails
     */
    List<FichierExcelDTO> findAll() throws CustomException;

    /**
     * Creates a new Excel file entry in the database.
     * @param fichierExcel Excel file DTO containing the data to be saved
     * @return Created Excel file DTO
     * @throws CustomException if creation fails
     */
    FichierExcelDTO create(FichierExcelDTO fichierExcel) throws CustomException;

    /**
     * Updates an existing Excel file entry.
     * @param id Excel file's unique identifier
     * @param fichierExcel Updated Excel file data
     * @return Updated Excel file DTO
     * @throws CustomException if update fails
     */
    FichierExcelDTO update(Long id, FichierExcelDTO fichierExcel) throws CustomException;

    /**
     * Deletes an Excel file entry by ID.
     * @param id Excel file's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Uploads and processes an Excel file.
     * @param file Excel file DTO
     * @throws CustomException if upload fails
     */
    void upload(FichierExcelDTO file) throws CustomException;

    /**
     * Retrieves the import history of all Excel files.
     * @return List of imported Excel files
     * @throws CustomException if retrieval fails
     */
    List<FichierExcelDTO> getImportHistory() throws CustomException;
}
