package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for student operations.
 * Manages student schedules and notifications.
 */
public interface EtudiantService {
    /**
     * Retrieves student details by ID.
     * @param id Student's unique identifier
     * @return Student DTO
     * @throws CustomException if student not found
     */
    EtudiantDTO findById(Long id) throws CustomException;

    /**
     * Retrieves student's personal schedule.
     * @param id Student's unique identifier
     * @return List of scheduled sessions
     * @throws CustomException if schedule retrieval fails
     */
    List<SeanceDTO> getPersonalSchedule(Long id) throws CustomException;

    /**
     * Retrieves schedule for specific branch.
     * @param brancheId Branch's unique identifier
     * @return List of scheduled sessions for branch
     * @throws CustomException if schedule retrieval fails
     */
    List<SeanceDTO> getBranchSchedule(Long brancheId) throws CustomException;

    /**
     * Retrieves student's notifications.
     * @param id Student's unique identifier
     * @return List of notifications
     * @throws CustomException if retrieval fails
     */
    List<NotificationDTO> getNotifications(Long id) throws CustomException;
}
