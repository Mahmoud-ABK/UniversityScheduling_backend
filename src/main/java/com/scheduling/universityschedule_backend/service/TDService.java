package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for tutorial group management.
 * Handles operations related to tutorial sessions.
 */
public interface TDService {
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
     * Retrieves practical sessions for tutorial group.
     * @param tdId Tutorial group's unique identifier
     * @return List of practical sessions
     * @throws CustomException if retrieval fails
     */
    List<TPDTO> getTPs(Long tdId) throws CustomException;
}
