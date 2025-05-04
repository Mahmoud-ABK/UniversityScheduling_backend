package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * REST Controller for student operations.
 * Handles endpoints related to schedules and notifications for students.
 *
 * @author Mahmoud-ABK
 * @version 1.0
 * @since 2025-05-04
 */
@RestController
@RequestMapping("/api/students")
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final NotificationService notificationService;

    @Autowired
    public EtudiantController(
            EtudiantService etudiantService,
            NotificationService notificationService) {
        this.etudiantService = etudiantService;
        this.notificationService = notificationService;
    }

    // ============================
    //    Profile Management
    // ============================

    @GetMapping("/{id}")
    public ResponseEntity<EtudiantDTO> getStudentProfile(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(etudiantService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtudiantDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody EtudiantDTO studentDTO) throws CustomException {
        return ResponseEntity.ok(etudiantService.update(id, studentDTO));
    }

    // ============================
    //    Schedule Access
    // ============================

    @GetMapping("/{id}/schedule/personal")
    public ResponseEntity<List<SeanceDTO>> getPersonalSchedule(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(etudiantService.getPersonalSchedule(id));
    }

    @GetMapping("/{id}/schedule/branch")
    public ResponseEntity<List<SeanceDTO>> getBranchSchedule(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(etudiantService.getBranchSchedule(id));
    }

    @GetMapping("/{id}/schedule/td")
    public ResponseEntity<List<SeanceDTO>> getTDSchedule(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(etudiantService.getTDSchedule(id));
    }

    // ============================
    //    Notifications
    // ============================

    @GetMapping("/{id}/notifications")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(etudiantService.getNotifications(id));
    }

    @GetMapping("/{id}/notifications/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() throws CustomException {
        return ResponseEntity.ok(notificationService.getUnreadNotifications());
    }

    @PutMapping("/notifications/{notificationId}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) throws CustomException {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}