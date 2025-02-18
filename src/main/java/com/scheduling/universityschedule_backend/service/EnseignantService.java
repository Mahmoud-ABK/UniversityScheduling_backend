package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for teacher operations.
 * Manages teacher schedules, teaching hours, and communication.
 */
public interface EnseignantService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves teacher details by ID.
     * @param id Teacher's unique identifier
     * @return Teacher DTO
     * @throws CustomException if teacher not found
     */
    EnseignantDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all teachers.
     * @return List of all teachers
     * @throws CustomException if retrieval fails
     */
    List<EnseignantDTO> findAll() throws CustomException;

    /**
     * Creates a new teacher.
     * @param enseignant Teacher DTO containing the new data
     * @return Created teacher DTO
     * @throws CustomException if creation fails
     */
    EnseignantDTO create(EnseignantDTO enseignant) throws CustomException;

    /**
     * Updates an existing teacher's information.
     * @param id Teacher's unique identifier
     * @param enseignant Updated teacher data
     * @return Updated teacher DTO
     * @throws CustomException if update fails
     */
    EnseignantDTO update(Long id, EnseignantDTO enseignant) throws CustomException;

    /**
     * Deletes a teacher by ID.
     * @param id Teacher's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Retrieves teacher's schedule.
     * @param id Teacher's unique identifier
     * @return List of scheduled sessions
     * @throws CustomException if schedule retrieval fails
     */
    List<SeanceDTO> getSchedule(Long id) throws CustomException;

    /**
     * Calculates total teaching hours.
     * @param teacherid Teacher's unique identifier
     * @param startdate the startdate of counting
     * @param enddate the enddate of counting
     * weekly sessions are counted once in a week
     * 1/15 session counts half per week
     * makeup session that have a date in frequency counts only once
     * @return Total hours taught between start and end date
     * @throws CustomException if calculation fails
     */
    int getTotalTeachingHours(Long teacherid,String startdate,String enddate) throws CustomException;

    /**
     * Submits makeup session request.
     * @param id Teacher's unique identifier
     * @param proposition Makeup session proposal DTO
     * @return Created makeup session proposal DTO
     * @throws CustomException if submission fails
     */
    PropositionDeRattrapageDTO submitMakeupRequest(Long id, PropositionDeRattrapageDTO proposition) throws CustomException;

    /**
     * Submits an issue or suggestion from a teacher.
     * @param id Teacher's unique identifier
     * @param signal Signal DTO containing issue or suggestion
     * @return Created signal DTO
     * @throws CustomException if submission fails
     */
    SignalDTO submitSignal(Long id, SignalDTO signal) throws CustomException;

    /**
     * Retrieves all signals submitted by a teacher.
     * @param id Teacher's unique identifier
     * @return List of submitted signals
     * @throws CustomException if retrieval fails
     */
    List<SignalDTO> getSignals(Long id) throws CustomException;

    /**
     * get all subjects taught for a teacher
     * @param id teacher id
     * @return all subject taught
     * @Exception return a CustomException
     */
    List<String> getSubjects(Long id) throws CustomException;

    /**
     * get all studentGroupsTaught Later to be mapped in
     * @param id teacher id
     * @return all studentgroups
     * @Exception return a CustomException
     */
    List<TPDTO> getStudentGroups(Long id) throws CustomException;


}
