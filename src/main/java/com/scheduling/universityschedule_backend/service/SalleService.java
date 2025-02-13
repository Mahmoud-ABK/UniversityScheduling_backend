package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for room management.
 * Handles CRUD operations and availability checking for rooms.
 */
public interface SalleService {
    /**
     * Retrieves all rooms.
     * @return List of all rooms
     * @throws CustomException if retrieval fails
     */
    List<SalleDTO> findAll() throws CustomException;

    /**
     * Retrieves room by ID.
     * @param id Room's unique identifier
     * @return Room DTO
     * @throws CustomException if room not found
     */
    SalleDTO findById(Long id) throws CustomException;

    /**
     * Creates new room.
     * @param salle Room to create
     * @return Created room
     * @throws CustomException if creation fails
     */
    SalleDTO create(SalleDTO salle) throws CustomException;

    /**
     * Updates existing room.
     * @param id Room's unique identifier
     * @param salle Updated room data
     * @return Updated room
     * @throws CustomException if update fails
     */
    SalleDTO update(Long id, SalleDTO salle) throws CustomException;

    /**
     * Deletes room.
     * @param id Room's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;

    /**
     * Finds available rooms for specific time slot.
     * @param date Desired date
     * @param startTime Start time
     * @param endTime End time
     * @return List of available rooms
     * @throws CustomException if search fails
     */
    List<SalleDTO> getAvailableRooms(String date, String startTime, String endTime) throws CustomException;
}
