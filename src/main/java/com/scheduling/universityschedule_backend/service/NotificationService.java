package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for notifications
 */
public interface NotificationService {
    List<NotificationDTO> getNotificationsForUser(Long userId) throws CustomException;
    void markAsRead(Long notificationId) throws CustomException ;
}
