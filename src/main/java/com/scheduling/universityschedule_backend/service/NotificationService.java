package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for notification management.
 * Handles system notifications and user alerts.
 */
public interface NotificationService {

    // ============================
    //          CRUD Operations
    // ============================

    /**
     * Retrieves a notification by ID.
     * @param id Notification's unique identifier
     * @return Notification DTO
     * @throws CustomException if notification not found
     */
    NotificationDTO findById(Long id) throws CustomException;

    /**
     * Retrieves all notifications.
     * @return List of all notifications
     * @throws CustomException if retrieval fails
     */
    List<NotificationDTO> findAll() throws CustomException;

    /**
     * Creates a new notification.
     * @param notification Notification DTO containing the data to be saved
     * @return Created Notification DTO
     * @throws CustomException if creation fails
     */
    NotificationDTO create(NotificationDTO notification) throws CustomException;

    /**
     * Updates an existing notification.
     * @param id Notification's unique identifier
     * @param notification Updated Notification DTO
     * @return Updated Notification DTO
     * @throws CustomException if update fails
     */
    NotificationDTO update(Long id, NotificationDTO notification) throws CustomException;

    /**
     * Deletes a notification by ID.
     * @param id Notification's unique identifier
     * @throws CustomException if deletion fails
     */
    void delete(Long id) throws CustomException;


    // ============================
    //          Functionalities
    // ============================

    /**
     * Marks a notification as read.
     * @param id Notification's unique identifier
     * @throws CustomException if update fails
     */
    void markAsRead(Long id) throws CustomException;

    /**
     * Retrieves unread notifications.
     * @return List of unread notifications
     * @throws CustomException if retrieval fails
     */
    List<NotificationDTO> getUnreadNotifications() throws CustomException;

    /**
     * Broadcasts a notification to all users.
     * @param notificationDTO Notification data to be broadcasted
     * @throws CustomException if broadcasting fails
     */
    void broadcastNotification(NotificationDTO notificationDTO) throws CustomException;
}
