package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for student operations.
 * Manages student schedules and notifications.
 */
public interface EtudiantService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves student details by ID.
     * @param id Student's unique identifier
     * @return Student DTO
     * @throws CustomException if student not found
     */
    EtudiantDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all students.
     * @return List of all students
     * @throws CustomException if retrieval fails
     */
    List<EtudiantDTO> findAll() throws CustomException;

    /**
     * Creates a new student.
     * @param etudiant Student DTO containing the new data
     * @return Created student DTO
     * @throws CustomException if creation fails
     */
    EtudiantDTO create(EtudiantDTO etudiant) throws CustomException;

    /**
     * Updates an existing student's information.
     * @param id Student's unique identifier
     * @param etudiant Updated student data
     * @return Updated student DTO
     * @throws CustomException if update fails
     */
    EtudiantDTO update(Long id, EtudiantDTO etudiant) throws CustomException;

    /**
     * Deletes a student by ID.
     * @param id Student's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Retrieves student's personal schedule.
     * @param id Student's unique identifier
     * @return List of scheduled sessions
     * @throws CustomException if schedule retrieval fails
     */
    List<SeanceDTO> getPersonalSchedule(Long id) throws CustomException;

    /**
     * Retrieves schedule for a specific branch.
     * @param id Student's unique identifier
     * @return List of scheduled sessions for branch
     * @throws CustomException if schedule retrieval fails
     */
    List<SeanceDTO> getBranchSchedule(Long id) throws CustomException;
    /**
     * Retrieves schedule for a specific branch.
     * @param id Student's unique identifier
     * @return List of scheduled sessions for branch
     * @throws CustomException if schedule retrieval fails
     */
    public List<SeanceDTO> getTDSchedule(Long id) throws CustomException;
    /**
     * Retrieves student's notifications.
     * @param id Student's unique identifier
     * @return List of notifications
     * @throws CustomException if retrieval fails
     */
    List<NotificationDTO> getNotifications(Long id) throws CustomException;
}
