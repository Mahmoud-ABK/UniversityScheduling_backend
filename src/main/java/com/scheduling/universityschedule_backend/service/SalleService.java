package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
     * @param date Desired date in format YYYY-MM-DD (can be null for recurring slots)
     * @param day Day of week (e.g., "MONDAY" or "lundi")
     * @param startTime Start time in format HH:mm
     * @param endTime End time in format HH:mm
     * @return List of available rooms during the specified time slot
     * @throws CustomException if search fails, time format is invalid, or parameters are inconsistent
     * @apiNote If date is not specified, the method will check for regular weekly availability
     */
    List<SalleDTO> getAvailableRooms(LocalDate date, DayOfWeek day, LocalTime startTime, LocalTime endTime) throws CustomException;



}
