package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notification);
    Optional<NotificationDTO> getNotificationById(Long id);
    List<NotificationDTO> getAllNotifications();
    NotificationDTO updateNotification(NotificationDTO notification) throws CustomException;
    void deleteNotification(Long id) throws CustomException;

    // Send notifications (e.g., push/email)
    void sendNotification(NotificationDTO notification);

    // Retrieve only the unread notifications for a user
    List<NotificationDTO> getUnreadNotifications(Long userId) throws CustomException;
}
