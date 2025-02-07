package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification createNotification(Notification notification);
    Optional<Notification> getNotificationById(Long id);
    List<Notification> getAllNotifications();
    Notification updateNotification(Notification notification) throws CustomException;
    void deleteNotification(Long id) throws CustomException;

    // Optionally, a method to send notifications (e.g., push/email)
    void sendNotification(Notification notification);
    // **New Method:**
    // Retrieve only the unread notifications for a user
    List<Notification> getUnreadNotifications(Long userId) throws CustomException;
}
