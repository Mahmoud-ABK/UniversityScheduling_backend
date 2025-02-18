package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for room management.
 * Handles CRUD operations and availability checking for rooms.
 */
public interface SalleService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves a room by its ID.
     * @param id Room's unique identifier
     * @return Room DTO
     * @throws CustomException if room not found
     */
    SalleDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all rooms.
     * @return List of all rooms
     * @throws CustomException if retrieval fails
     */
    List<SalleDTO> findAll() throws CustomException;

    /**
     * Creates a new room.
     * @param salle Room data to be saved
     * @return Created room
     * @throws CustomException if creation fails
     */
    SalleDTO create(SalleDTO salle) throws CustomException;

    /**
     * Updates an existing room.
     * @param id Room's unique identifier
     * @param salle Updated room data
     * @return Updated room
     * @throws CustomException if update fails
     */
    SalleDTO update(Long id, SalleDTO salle) throws CustomException;

    /**
     * Deletes a room.
     * @param id Room's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Finds available rooms for a specific time slot.
     * @param date Desired date
     * @param startTime Start time
     * @param day day
     * @param endTime End time
     * @return List of available rooms
     * @throws CustomException if search fails
     */
    List<SalleDTO> getAvailableRooms(String date, String day,String startTime, String endTime) throws CustomException;




}
