package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for teacher operations.
 * Handles endpoints related to teaching schedules, makeup requests, and communications.
 *
 * @author Mahmoud-ABK
 * @version 1.0
 * @since 2025-05-04
 */
@RestController
@RequestMapping("/api/teachers")
public class EnseignantController {

    private final EnseignantService enseignantService;
    private final NotificationService notificationService;

    @Autowired
    public EnseignantController(
            EnseignantService enseignantService,
            NotificationService notificationService) {
        this.enseignantService = enseignantService;
        this.notificationService = notificationService;
    }

    // ============================
    //    Profile Management
    // ============================

    @GetMapping("/{id}")
    public ResponseEntity<EnseignantDTO> getTeacherProfile(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(enseignantService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnseignantDTO> updateProfile(
            @PathVariable Long id,
            @RequestBody EnseignantDTO teacherDTO) throws CustomException {
        return ResponseEntity.ok(enseignantService.update(id, teacherDTO));
    }

    // ============================
    //    Schedule Management
    // ============================

    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<SeanceDTO>> getSchedule(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(enseignantService.getSchedule(id));
    }

    @GetMapping("/{id}/teaching-hours")
    public ResponseEntity<Integer> getTeachingHours(
            @PathVariable Long id,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) throws CustomException {
        return ResponseEntity.ok(enseignantService.getTotalTeachingHours(id, startDate, endDate));
    }

    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<String>> getSubjects(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(enseignantService.getSubjects(id));
    }

    @GetMapping("/{id}/student-groups")
    public ResponseEntity<List<TPDTO>> getStudentGroups(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(enseignantService.getStudentGroups(id));
    }

    // ============================
    //    Makeup Sessions
    // ============================

    @PostMapping("/{id}/makeup-requests")
    public ResponseEntity<PropositionDeRattrapageDTO> submitMakeupRequest(
            @PathVariable Long id,
            @RequestBody PropositionDeRattrapageDTO propositionDTO) throws CustomException {
        return ResponseEntity.ok(enseignantService.submitMakeupRequest(id, propositionDTO));
    }

    // ============================
    //    Communication
    // ============================

    @PostMapping("/{id}/signals")
    public ResponseEntity<SignalDTO> submitSignal(
            @PathVariable Long id,
            @RequestBody SignalDTO signalDTO) throws CustomException {
        return ResponseEntity.ok(enseignantService.submitSignal(id, signalDTO));
    }

    @GetMapping("/{id}/signals")
    public ResponseEntity<List<SignalDTO>> getSignals(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(enseignantService.getSignals(id));
    }

    // ============================
    //    Notifications
    // ============================

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