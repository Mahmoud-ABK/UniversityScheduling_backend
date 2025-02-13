package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for branch management.
 * Handles CRUD operations for academic branches/specializations.
 */
public interface BrancheService {
    /**
     * Retrieves all branches.
     * @return List of all branches
     * @throws CustomException if retrieval fails
     */
    List<BrancheDTO> findAll() throws CustomException;

    /**
     * Retrieves branch by ID.
     * @param id Branch's unique identifier
     * @return Branch DTO
     * @throws CustomException if branch not found
     */
    BrancheDTO findById(Long id) throws CustomException;

    /**
     * Creates new branch.
     * @param branche Branch to create
     * @return Created branch
     * @throws CustomException if creation fails
     */
    BrancheDTO create(BrancheDTO branche) throws CustomException;

    /**
     * Updates existing branch.
     * @param id Branch's unique identifier
     * @param branche Updated branch data
     * @return Updated branch
     * @throws CustomException if update fails
     */
    BrancheDTO update(Long id, BrancheDTO branche) throws CustomException;

    /**
     * Deletes branch.
     * @param id Branch's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;
}
