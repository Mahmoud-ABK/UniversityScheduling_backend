package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;

/**
 * Service interface for notification management.
 * Handles system notifications and user alerts.
 */
public interface NotificationService {
    /**
     * Retrieves all notifications.
     * @return List of all notifications
     * @throws CustomException if retrieval fails
     */
    List<NotificationDTO> findAll() throws CustomException;

    /**
     * Marks notification as read.
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
    public void broadcastNotification(NotificationDTO notificationDTO) throws CustomException ;
}

