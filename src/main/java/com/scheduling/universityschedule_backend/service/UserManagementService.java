package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.UserDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

public interface UserManagementService {

    /**
     * Retrieves a user by ID.
     * @param id User's unique identifier (UserCredentials ID)
     * @return User DTO
     * @throws CustomException if user not found
     */
    UserDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all users.
     * @return List of all users
     * @throws CustomException if retrieval fails
     */
    List<UserDTO> findAll() throws CustomException;

    /**
     * Creates a new user.
     * @param userDTO User DTO containing the new data
     * @return Created user DTO
     * @throws CustomException if creation fails
     */
    UserDTO create(UserDTO userDTO) throws CustomException;

    /**
     * Updates an existing user.
     * @param id User's unique identifier
     * @param userDTO Updated user data
     * @return Updated user DTO
     * @throws CustomException if update fails
     */
    UserDTO update(Long id, UserDTO userDTO) throws CustomException;

    /**
     * Deletes a user by ID.
     * @param id User's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;

    /**
     * Retrieves a user by Personne ID.
     * @param personneId Personne's unique identifier
     * @return User DTO with role-specific Personne data
     * @throws CustomException if user or Personne not found
     */
    UserDTO findByPersonneId(Long personneId) throws CustomException;
}