package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for managing academic branches.
 * Includes CRUD operations and additional branch-related functionalities.
 */
public interface BrancheService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves branch details by ID.
     * @param id Branch's unique identifier
     * @return Branch DTO
     * @throws CustomException if branch not found
     */
    BrancheDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all branches.
     * @return List of all branches
     * @throws CustomException if retrieval fails
     */
    List<BrancheDTO> findAll() throws CustomException;

    /**
     * Creates a new branch.
     * @param branche Branch DTO containing the new data
     * @return Created branch DTO
     * @throws CustomException if creation fails
     */
    BrancheDTO create(BrancheDTO branche) throws CustomException;

    /**
     * Updates an existing branch.
     * @param id Branch's unique identifier
     * @param branche Updated branch data
     * @return Updated branch DTO
     * @throws CustomException if update fails
     */
    BrancheDTO update(Long id, BrancheDTO branche) throws CustomException;

    /**
     * Deletes a branch by ID.
     * @param id Branch's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================


    /**
     * Retrieves all seances associated with a branch.
     * @param branchId Branch's unique identifier
     * @return List of courses associated with the branch
     * @throws CustomException if retrieval fails
     */
    List<SeanceDTO> getSchedule(Long branchId) throws CustomException;

    /**
     * Retrieves all students enrolled in a specific branch.
     * @param id Branch's unique identifier
     * @return List of students enrolled in this branch
     * @throws CustomException if retrieval fails or branch doesn't exist
     */
    List<EtudiantDTO> getEtudiants(Long id) throws CustomException;
}
