package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for user management.
 * Handles general user operations accessible to technicians.
 */
public interface UserService {
    /**
     * Retrieves all users.
     * @return List of all users
     * @throws CustomException if retrieval fails
     */
    List<PersonneDTO> findAll() throws CustomException;

    /**
     * Creates new user.
     * @param user User to createf
     * @return Created user
     * @throws CustomException if creation fails
     */
    PersonneDTO create(PersonneDTO user) throws CustomException;

    /**
     * Updates existing user.
     * @param id User's unique identifier
     * @param user Updated user data
     * @return Updated user
     * @throws CustomException if update fails
     */
    PersonneDTO update(Long id, PersonneDTO user) throws CustomException;

    /**
     * Resets user's password.
     * @param id User's unique identifier
     * @param newPassword New password
     * @throws CustomException if reset fails
     */
    void resetPassword(Long id, String newPassword) throws CustomException;
}
