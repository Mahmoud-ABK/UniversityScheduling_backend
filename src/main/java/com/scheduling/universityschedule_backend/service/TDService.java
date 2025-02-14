package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for tutorial group management.
 * Handles operations related to tutorial sessions.
 */
public interface TDService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves all tutorial groups.
     * @return List of all tutorial groups
     * @throws CustomException if retrieval fails
     */
    List<TDDTO> findAll() throws CustomException;

    /**
     * Retrieves tutorial group by ID.
     * @param id Tutorial group's unique identifier
     * @return Tutorial group DTO
     * @throws CustomException if group not found
     */
    TDDTO findById(Long id) throws CustomException;

    /**
     * Creates a new tutorial group.
     * @param td Tutorial group data to be created
     * @return Created tutorial group DTO
     * @throws CustomException if creation fails
     */
    TDDTO create(TDDTO td) throws CustomException;

    /**
     * Updates an existing tutorial group.
     * @param id Tutorial group's unique identifier
     * @param td Updated tutorial group data
     * @return Updated tutorial group DTO
     * @throws CustomException if update fails
     */
    TDDTO update(Long id, TDDTO td) throws CustomException;

    /**
     * Deletes a tutorial group.
     * @param id Tutorial group's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Retrieves practical sessions for a specific tutorial group.
     * @param tdId Tutorial group's unique identifier
     * @return List of practical sessions (TP)
     * @throws CustomException if retrieval fails
     */
    List<TPDTO> getTPs(Long tdId) throws CustomException;
}
