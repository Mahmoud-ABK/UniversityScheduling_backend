package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification createNotification(Notification notification);
    Optional<Notification> getNotificationById(Long id);
    List<Notification> getAllNotifications();
    Notification updateNotification(Notification notification);
    void deleteNotification(Long id);

    // Optionally, a method to send notifications (e.g., push/email)
    void sendNotification(Notification notification);
}
