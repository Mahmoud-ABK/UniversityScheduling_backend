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
     * Retrieves all makeup session requests.
     * @return List of makeup session proposals
     * @throws CustomException if retrieval fails
     */
    List<PropositionDeRattrapageDTO> getAllMakeupSessions() throws CustomException;

    /**
     * Processes initial makeup session requests (Admin only).
     * Status Flow: PENDING -> SCHEDULED or APPROVED
     *
     * @param id      Proposal ID
     * @param salleId Optional room ID. If null, sets status to SCHEDULED
     * @return Updated proposal
     * @throws CustomException if proposal isn't PENDING or other errors occur
     */
    PropositionDeRattrapageDTO approveMakeupSession(Long id, Long salleId) throws CustomException;

    /**
     * Rejects a PENDING makeup session proposal (Admin only).
     * Status Flow: PENDING -> REJECTED
     *
     * @param id Proposal ID
     * @return Updated proposal
     * @throws CustomException if proposal isn't PENDING or other errors occur
     */
    PropositionDeRattrapageDTO rejectMakeupSession(Long id) throws CustomException;

    /**
     * Approves a SCHEDULED makeup session (Technician/Admin).
     * Status Flow: SCHEDULED -> APPROVED
     *
     * @param id      Proposal ID
     * @param salleId Room ID (required)
     * @return Updated proposal with created Seance
     * @throws CustomException if proposal isn't SCHEDULED or other errors occur
     */
    PropositionDeRattrapageDTO approveScheduled(Long id, Long salleId) throws CustomException;

    /**
     * Rejects a SCHEDULED makeup session (Technician/Admin).
     * Status Flow: SCHEDULED -> REJECTED
     *
     * @param id     Proposal ID
     * @param reason Rejection reason
     * @return Updated proposal
     * @throws CustomException if proposal isn't SCHEDULED or other errors occur
     */
    PropositionDeRattrapageDTO rejectScheduled(Long id, String reason) throws CustomException;


}
