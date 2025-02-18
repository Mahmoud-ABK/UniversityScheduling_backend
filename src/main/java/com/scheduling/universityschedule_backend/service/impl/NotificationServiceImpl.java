package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public NotificationDTO findById(Long id) throws CustomException {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));
        return entityMapper.toNotificationDTO(notification);
    }

    @Override
    public List<NotificationDTO> findAll() throws CustomException {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream()
                .map(entityMapper::toNotificationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO create(NotificationDTO notificationDTO) throws CustomException {
        Notification notification = entityMapper.toNotification(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return entityMapper.toNotificationDTO(savedNotification);
    }

    @Override
    public NotificationDTO update(Long id, NotificationDTO notificationDTO) throws CustomException {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));
        entityMapper.updateFromDto(notificationDTO, existingNotification);
        Notification updatedNotification = notificationRepository.save(existingNotification);
        return entityMapper.toNotificationDTO(updatedNotification);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!notificationRepository.existsById(id)) {
            throw new CustomException("Notification not found with ID: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    public void markAsRead(Long id) throws CustomException {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));
        notification.setIsread(true);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDTO> getUnreadNotifications() throws CustomException {
        List<Notification> unreadNotifications = notificationRepository.findByIsreadFalse();
        return unreadNotifications.stream()
                .map(entityMapper::toNotificationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void broadcastNotification(NotificationDTO notificationDTO) throws CustomException {
        // Implement logic to send notification to all users (Teachers + Students not admins and Technicians)
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void sendNotificationToTeachers(NotificationDTO notificationDTO) throws CustomException {
        // Implement logic to send notification to all teachers
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void sendNotificationToStudents(NotificationDTO notificationDTO) throws CustomException {
        // Implement logic to send notification to all students
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void sendNotificationToBranches(NotificationDTO notificationDTO, List<BrancheDTO> brancheDTOS) throws CustomException {
        // Implement logic to send notification to all branches
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void sendNotificationToBranche(NotificationDTO notificationDTO, BrancheDTO brancheDTO) throws CustomException {
        // Implement logic to send notification to a specific branch
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void sendNotificationToTDs(NotificationDTO notificationDTO, List<TDDTO> tddtos) throws CustomException {
        // Implement logic to send notification to tutorial groups
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void sendNotificationToTD(NotificationDTO notificationDTO, TDDTO tddto) throws CustomException {
        // Implement logic to send notification to a specific tutorial group
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void sendNotificationToTPs(NotificationDTO notificationDTO, List<TPDTO> tpdtos) throws CustomException {
        // Implement logic to send notification to practical sessions
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void sendNotificationToTP(NotificationDTO notificationDTO, TPDTO tpdto) throws CustomException {
        // Implement logic to send notification to a specific practical session
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}