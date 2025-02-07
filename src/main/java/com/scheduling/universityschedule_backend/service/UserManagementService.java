package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.Personne;

import java.util.List;

public interface UserManagementService {
    // Create a new user account (Personne can encapsulate necessary fields like type/role)
    Personne createUser(Personne personne);

    // Update user details
    Personne updateUser(Long userId, Personne personne) throws CustomException;

    // Delete a user account
    void deleteUser(Long userId) throws CustomException;

    // Retrieve a single user by ID
    Personne getUserById(Long userId) throws CustomException;

    // Retrieve all users (optionally with filtering by role)
    List<Personne> getAllUsers();

    // Assign or update a user's role (e.g., "Etudiant", "Enseignant", "Administrateur", "Technicien")
    void assignRole(Long userId, String role) throws CustomException;
}
