package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for session management.
 * Handles CRUD operations and conflict detection for teaching sessions.
 */
public interface SeanceService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves all teaching sessions.
     * @return List of all sessions
     * @throws CustomException if retrieval fails
     */
    List<SeanceDTO> findAll() throws CustomException;

    /**
     * Retrieves session by ID.
     * @param id Session's unique identifier
     * @return Session DTO
     * @throws CustomException if session not found
     */
    SeanceDTO findById(Long id) throws CustomException;

    /**
     * Creates a new teaching session.
     * @param seance Session data to be created
     * @return Created session
     * @throws CustomException if creation fails
     */
    SeanceDTO create(SeanceDTO seance) throws CustomException;

    /**
     * Updates an existing teaching session.
     * @param id Session's unique identifier
     * @param seance Updated session data
     * @return Updated session
     * @throws CustomException if update fails
     */
    SeanceDTO update(Long id, SeanceDTO seance) throws CustomException;

    /**
     * Deletes a teaching session.
     * @param id Session's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Retrieves all session conflicts.
     * @return List of session conflicts
     * @throws CustomException if retrieval fails
     */
    List<SeanceConflictDTO> getAllConflicts() throws CustomException;


    /**
     * Retrieves all room conflicts.
     * @return List of room conflicts
     * @throws CustomException if retrieval fails
     */
    List<SeanceRoomConflictDTO> getRoomConflicts() throws CustomException;

    /**
     * Retrieves conflicts for a specific session.
     * @param seanceId Session's unique identifier
     * @return List of conflicts for the given session
     * @throws CustomException if retrieval fails
     */
    List<SingleSeanceConflictDTO> getConflictsForSession(Long seanceId) throws CustomException;


}
