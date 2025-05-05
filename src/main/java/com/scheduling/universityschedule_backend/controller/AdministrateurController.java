package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.service.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * REST Controller for administrator operations.
 * Handles endpoints related to schedule management, makeup sessions, and notifications.
 *
 * @author Mahmoud-ABK
 * @version 1.0
 * @since 2025-05-04
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdministrateurController {

    private final AdministrateurService administrateurService;
    private final SeanceService seanceService;
    private final NotificationService notificationService;
    private final ExcelFileService excelFileService;

    @Autowired
    public AdministrateurController(
            AdministrateurService administrateurService,
            SeanceService seanceService,
            NotificationService notificationService,
            ExcelFileService excelFileService) {
        this.administrateurService = administrateurService;
        this.seanceService = seanceService;
        this.notificationService = notificationService;
        this.excelFileService = excelFileService;
    }

    // ============================
    //    Schedule Management
    // ============================

    @GetMapping("/seances")
    public ResponseEntity<List<SeanceDTO>> getAllSeances() throws CustomException {
        return ResponseEntity.ok(seanceService.findAll());
    }

    @GetMapping("/seances/{id}")
    public ResponseEntity<SeanceDTO> getSeanceById(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(seanceService.findById(id));
    }

    @PostMapping("/seances")
    public ResponseEntity<SeanceDTO> createSeance(@RequestBody SeanceDTO seanceDTO) throws CustomException {
        return ResponseEntity.ok(seanceService.create(seanceDTO));
    }

    @PutMapping("/seances/{id}")
    public ResponseEntity<SeanceDTO> updateSeance(@PathVariable Long id, @RequestBody SeanceDTO seanceDTO) throws CustomException {
        return ResponseEntity.ok(seanceService.update(id, seanceDTO));
    }

    @DeleteMapping("/seances/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) throws CustomException {
        seanceService.delete(id);
        return ResponseEntity.ok().build();
    }

    // ============================
    //    Makeup Session Management
    // ============================

    @GetMapping("/makeup-sessions")
    public ResponseEntity<List<PropositionDeRattrapageDTO>> getAllMakeupSessions() throws CustomException {
        return ResponseEntity.ok(administrateurService.getAllMakeupSessions());
    }

    @PutMapping("/makeup-sessions/{id}/approve")
    public ResponseEntity<PropositionDeRattrapageDTO> approveMakeupSession(
            @PathVariable Long id,
            @RequestParam(required = false) Long salleId) throws CustomException {
        return ResponseEntity.ok(administrateurService.approveMakeupSession(id, salleId));
    }

    @PutMapping("/makeup-sessions/{id}/reject")
    public ResponseEntity<PropositionDeRattrapageDTO> rejectMakeupSession(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(administrateurService.rejectMakeupSession(id));
    }

    @PutMapping("/makeup-sessions/{id}/approve-scheduled")
    public ResponseEntity<PropositionDeRattrapageDTO> approveScheduledSession(
            @PathVariable Long id,
            @RequestParam Long salleId) throws CustomException {
        return ResponseEntity.ok(administrateurService.approveScheduled(id, salleId));
    }

    @PutMapping("/makeup-sessions/{id}/reject-scheduled")
    public ResponseEntity<PropositionDeRattrapageDTO> rejectScheduledSession(
            @PathVariable Long id,
            @RequestParam String reason) throws CustomException {
        return ResponseEntity.ok(administrateurService.rejectScheduled(id, reason));
    }

    // ============================
    //    Conflict Management
    // ============================

    @GetMapping("/conflicts")
    public ResponseEntity<List<SeanceConflictDTO>> getAllConflicts() throws CustomException {
        return ResponseEntity.ok(seanceService.getAllConflicts());
    }

    @GetMapping("/conflicts/rooms")
    public ResponseEntity<List<SeanceRoomConflictDTO>> getRoomConflicts() throws CustomException {
        return ResponseEntity.ok(seanceService.getRoomConflicts());
    }

    @GetMapping("/conflicts/seances/{seanceId}")
    public ResponseEntity<List<SingleSeanceConflictDTO>> getConflictsForSession(@PathVariable Long seanceId) throws CustomException {
        return ResponseEntity.ok(seanceService.getConflictsForSession(seanceId));
    }

    // ============================
    //    Notification Management
    // ============================

    @PostMapping("/notifications/broadcast")
    public ResponseEntity<Void> broadcastNotification(@RequestBody NotificationDTO notification) throws CustomException {
        notificationService.broadcastNotification(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/teachers")
    public ResponseEntity<Void> notifyTeachers(@RequestBody NotificationDTO notification) throws CustomException {
        notificationService.sendNotificationToTeachers(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/students")
    public ResponseEntity<Void> notifyStudents(@RequestBody NotificationDTO notification) throws CustomException {
        notificationService.sendNotificationToStudents(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/branches")
    public ResponseEntity<Void> notifyBranches(
            @RequestBody NotificationDTO notification,
            @RequestParam List<Long> branchIds) throws CustomException {
        // Note: You'll need to fetch BrancheDTOs using branchIds
        notificationService.sendNotificationToBranches(notification, null); // Implementation needed
        return ResponseEntity.ok().build();
    }

    // ============================
    //    Excel File Management
    // ============================

    @PostMapping("/excel/upload")
    public ResponseEntity<Void> uploadExcelFile(
            @RequestBody FichierExcelDTO file,
            @RequestBody List<SeanceDTO> seances) throws CustomException {
        excelFileService.upload(file, seances);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/excel/history")
    public ResponseEntity<List<FichierExcelDTO>> getImportHistory() throws CustomException {
        return ResponseEntity.ok(excelFileService.getImportHistory());
    }
}