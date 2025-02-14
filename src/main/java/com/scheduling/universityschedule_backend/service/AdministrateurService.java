package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for administrator operations.
 * Handles schedule management, makeup sessions, and system-wide notifications.
 */
public interface AdministrateurService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves administrator details by ID.
     * @param id Administrator's unique identifier
     * @return Administrator DTO
     * @throws CustomException if administrator not found
     */
    AdministrateurDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all administrators.
     * @return List of all administrators
     * @throws CustomException if retrieval fails
     */
    List<AdministrateurDTO> findAll() throws CustomException;

    /**
     * Creates a new administrator.
     * @param administrateur Administrator DTO containing the new data
     * @return Created administrator DTO
     * @throws CustomException if creation fails
     */
    AdministrateurDTO create(AdministrateurDTO administrateur) throws CustomException;

    /**
     * Updates an existing administrator.
     * @param id Administrator's unique identifier
     * @param administrateur Updated administrator data
     * @return Updated administrator DTO
     * @throws CustomException if update fails
     */
    AdministrateurDTO update(Long id, AdministrateurDTO administrateur) throws CustomException;

    /**
     * Deletes an administrator by ID.
     * @param id Administrator's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Imports schedule data from Excel file.
     * @param fichier Excel file DTO containing schedule data
     * @throws CustomException if import fails
     */
    void importExcelSchedule(FichierExcelDTO fichier) throws CustomException;

    /**
     * Automatically generates schedule based on constraints.
     * @throws CustomException if generation fails
     */
    void generateSchedule() throws CustomException;

    /**
     * Retrieves all makeup session requests.
     * @return List of makeup session proposals
     * @throws CustomException if retrieval fails
     */
    List<PropositionDeRattrapageDTO> getAllMakeupSessions() throws CustomException;

    /**
     * Approves a makeup session request.
     * @param id Makeup session proposal ID
     * @throws CustomException if approval fails
     */
    void approveMakeupSession(Long id) throws CustomException;

    /**
     * Rejects a makeup session request.
     * @param id Makeup session proposal ID
     * @throws CustomException if rejection fails
     */
    void rejectMakeupSession(Long id) throws CustomException;

    /**
     * Sends notification to all system users.
     * @param notification Notification to broadcast
     * @throws CustomException if broadcast fails
     */
    void broadcastNotification(NotificationDTO notification) throws CustomException;

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
    List<SeanceConflictDTO> getRoomConflicts() throws CustomException;
}
