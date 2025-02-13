package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for practical session management.
 * Handles operations related to practical/lab sessions.
 */
public interface TPService {
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
     * Retrieves students enrolled in practical session.
     * @param tpId Practical session's unique identifier
     * @return List of enrolled students
     * @throws CustomException if retrieval fails
     */
    List<EtudiantDTO> getStudents(Long tpId) throws CustomException;
}
