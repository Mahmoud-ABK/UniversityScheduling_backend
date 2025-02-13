package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for teacher operations.
 * Manages teacher schedules, teaching hours, and communication.
 */
public interface EnseignantService {
    /**
     * Retrieves teacher details by ID.
     * @param id Teacher's unique identifier
     * @return Teacher DTO
     * @throws CustomException if teacher not found
     */
    EnseignantDTO findById(Long id) throws CustomException;

    /**
     * Retrieves teacher's schedule.
     * @param id Teacher's unique identifier
     * @return List of scheduled sessions
     * @throws CustomException if schedule retrieval fails
     */
    List<SeanceDTO> getSchedule(Long id) throws CustomException;

    /**
     * Calculates total teaching hours.
     * @param id Teacher's unique identifier
     * @return Total hours taught
     * @throws CustomException if calculation fails
     */
    int getTotalTeachingHours(Long id) throws CustomException;

    /**
     * Submits makeup session request.
     * @param id Teacher's unique identifier
     * @param proposition Makeup session proposal
     * @return Created makeup session proposal
     * @throws CustomException if submission fails
     */
    PropositionDeRattrapageDTO submitMakeupRequest(Long id, PropositionDeRattrapageDTO proposition) throws CustomException;

    /**
     * Submits issue or suggestion.
     * @param id Teacher's unique identifier
     * @param signal Signal containing issue/suggestion
     * @return Created signal
     * @throws CustomException if submission fails
     */
    SignalDTO submitSignal(Long id, SignalDTO signal) throws CustomException;

    /**
     * Retrieves teacher's submitted signals.
     * @param id Teacher's unique identifier
     * @return List of submitted signals
     * @throws CustomException if retrieval fails
     */
    List<SignalDTO> getSignals(Long id) throws CustomException;
}
