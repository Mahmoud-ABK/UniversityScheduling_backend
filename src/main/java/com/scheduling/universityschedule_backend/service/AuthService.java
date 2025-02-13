package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface handling authentication and user session management.
 * Provides functionality for user login, logout, token management, and password operations.
 */
public interface AuthService {
    /**
     * Authenticates a user and generates a JWT token.
     * @param email User's email address
     * @param password User's password
     * @return JWT token for authenticated session
     * @throws CustomException if authentication fails
     */
    String login(String email, String password) throws CustomException;

    /**
     * Invalidates the user's current session token.
     * @param token Current JWT token to invalidate
     * @throws CustomException if token invalidation fails
     */
    void logout(String token) throws CustomException;

    /**
     * Generates a new JWT token from an expired token.
     * @param token Expired JWT token
     * @return New valid JWT token
     * @throws CustomException if token refresh fails
     */
    String refreshToken(String token) throws CustomException;

    /**
     * Initiates password reset process by sending reset link.
     * @param email User's email address
     * @throws CustomException if reset initiation fails
     */
    void resetPassword(String email) throws CustomException;

    /**
     * Updates user's password using reset token.
     * @param token Password reset token
     * @param password New password
     * @throws CustomException if password update fails
     */
    void setNewPassword(String token, String password) throws CustomException;
}
