package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.TechnicienDTO;
import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for technician operations.
 * Focuses on technical maintenance and resource management.
 * Note: User management functionality will be handled separately by authentication services.
 *
 * @author Mahmoud-ABK
 * @version 1.0
 * @since 2025-05-04
 */
public interface TechnicienService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves technician details by ID.
     *
     * @param id Technician's unique identifier
     * @return Technician DTO
     * @throws CustomException if technician not found
     */
    TechnicienDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all technicians.
     *
     * @return List of all technicians
     * @throws CustomException if retrieval fails
     */
    List<TechnicienDTO> findAll() throws CustomException;

    /**
     * Creates a new technician.
     *
     * @param technicien Technician DTO containing the new data
     * @return Created technician DTO
     * @throws CustomException if creation fails
     */
    TechnicienDTO create(TechnicienDTO technicien) throws CustomException;

    /**
     * Updates an existing technician's information.
     *
     * @param id         Technician's unique identifier
     * @param technicien Updated technician data
     * @return Updated technician DTO
     * @throws CustomException if update fails
     */
    TechnicienDTO update(Long id, TechnicienDTO technicien) throws CustomException;

    /**
     * Deletes a technician by ID.
     *
     * @param id Technician's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;

}