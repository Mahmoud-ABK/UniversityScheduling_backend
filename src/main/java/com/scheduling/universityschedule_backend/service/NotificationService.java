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
     * @param notificationDTO Notification data to be broadcasted usually recepteur is empty
     * @throws CustomException if broadcasting fails
     */
    void broadcastNotification(NotificationDTO notificationDTO) throws CustomException;

    /**
     * Sends a notification to all teachers in the system.
     * @param notificationDTO Notification content and metadata
     * @throws CustomException if sending fails or notification is invalid
     */
    void sendNotificationToTeachers(NotificationDTO notificationDTO) throws CustomException;


    /**
     * Sends a notification to all students in the system.
     * @param notificationDTO Notification content and metadata
     * @throws CustomException if sending fails or notification is invalid
     */
    void sendNotificationToStudents(NotificationDTO notificationDTO) throws CustomException;

    /**
     * Sends a notification to students in multiple branches.
     * @param notificationDTO Notification content and metadata
     * @param brancheDTOS List of branches whose students should receive the notification
     * @throws CustomException if sending fails or any input is invalid
     */
    void sendNotificationToBranches(NotificationDTO notificationDTO,List<BrancheDTO> brancheDTOS) throws CustomException;

    /**
     * Sends a notification to all students in a specific branch.
     * @param notificationDTO Notification content and metadata
     * @param brancheDTOS Branch whose students should receive the notification
     * @throws CustomException if sending fails or any input is invalid
     */
    void sendNotificationToBranche(NotificationDTO notificationDTO,BrancheDTO brancheDTOS) throws CustomException;

    /**
     * Sends a notification to students in multiple tutorial groups (TDs).
     * @param notificationDTO Notification content and metadata
     * @param tddtos List of tutorial groups whose students should receive the notification
     * @throws CustomException if sending fails or any input is invalid
     */
    void sendNotificationToTDs(NotificationDTO notificationDTO,List<TDDTO> tddtos) throws CustomException;

    /**
     * Sends a notification to all students in a specific tutorial group (TD).
     * @param notificationDTO Notification content and metadata
     * @param tddto Tutorial group whose students should receive the notification
     * @throws CustomException if sending fails or any input is invalid
     */
    void sendNotificationToTD(NotificationDTO notificationDTO,TDDTO tddto) throws CustomException;

    /**
     * Sends a notification to students in multiple practical groups (TPs).
     * @param notificationDTO Notification content and metadata
     * @param tpdtos List of practical groups whose students should receive the notification
     * @throws CustomException if sending fails or any input is invalid
     */
    void sendNotificationToTPs(NotificationDTO notificationDTO,List<TPDTO> tpdtos) throws CustomException;

    /**
     * Sends a notification to all students in a specific practical group (TP).
     * @param notificationDTO Notification content and metadata
     * @param tpdto Practical group whose students should receive the notification
     * @throws CustomException if sending fails or any input is invalid
     */
    void sendNotificationToTP(NotificationDTO notificationDTO,TPDTO tpdto) throws CustomException;


}
