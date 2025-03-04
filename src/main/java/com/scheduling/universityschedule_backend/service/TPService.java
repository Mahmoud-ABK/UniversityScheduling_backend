package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for practical session management.
 * Handles operations related to practical/lab sessions.
 */
public interface TPService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves all practical sessions.
     * @return List of all practical sessions
     * @throws CustomException if retrieval fails
     */
    List<TPDTO> findAll() throws CustomException;

    /**
     * Retrieves practical session by ID.
     * @param id Practical session's unique identifier
     * @return Practical session DTO
     * @throws CustomException if session not found
     */
    TPDTO findById(Long id) throws CustomException;

    /**
     * Creates a new practical session.
     * @param tp Practical session data to be created
     * @return Created practical session DTO
     * @throws CustomException if creation fails
     */
    TPDTO create(TPDTO tp) throws CustomException;

    /**
     * Updates an existing practical session.
     * @param id Practical session's unique identifier
     * @param tp Updated practical session data
     * @return Updated practical session DTO
     * @throws CustomException if update fails
     */
    TPDTO update(Long id, TPDTO tp) throws CustomException;

    /**
     * Deletes a practical session.
     * @param id Practical session's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Retrieves students enrolled in a practical session.
     * @param tpId Practical session's unique identifier
     * @return List of students enrolled in the practical session
     * @throws CustomException if retrieval fails
     */
    List<EtudiantDTO> getStudents(Long tpId) throws CustomException;

    /**
     * Generates a complete schedule for a practical group (TP), including its TD and branch sessions.
     * @param id Practical group's (TP) unique identifier
     * @return List of sessions (Seances) in the generated schedule
     * @throws CustomException if generation fails or TP doesn't exist
     */
    List<SeanceDTO> generateSchedule(Long id) throws CustomException;

    /**
     * Retrieves all students enrolled in a specific practical group (TP).
     * @param id Practical group's (TP) unique identifier
     * @return List of students enrolled in this practical group
     * @throws CustomException if retrieval fails or TP doesn't exist
     */
    List<EtudiantDTO> getEtudiants(Long id) throws CustomException;


}
