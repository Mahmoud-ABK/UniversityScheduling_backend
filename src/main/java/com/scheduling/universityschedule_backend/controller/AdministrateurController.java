package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final BrancheService brancheService;

    @Autowired
    public AdministrateurController(
            AdministrateurService administrateurService,
            SeanceService seanceService,
            NotificationService notificationService,
            ExcelFileService excelFileService, BrancheService brancheService) {
        this.administrateurService = administrateurService;
        this.seanceService = seanceService;
        this.notificationService = notificationService;
        this.excelFileService = excelFileService;
        this.brancheService = brancheService;
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

    @PostMapping("/seances/batch")
    public ResponseEntity<BatchDTO> createSeancesBatch(@Valid @RequestBody List<SeanceDTO> seanceDTOs) throws CustomException {
        List<Long> createdIds = new ArrayList<>();
        seanceDTOs.forEach(seanceDTO -> {
            try {
                createdIds.add(seanceService.create(seanceDTO).getId());
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
        });

        return ResponseEntity.ok(new BatchDTO(createdIds, "Batch creation successful", true, "Seance"));
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
            @Valid @RequestBody NotificationDTO notification,
            @RequestParam List<Long> branchIds) throws CustomException {
        List<BrancheDTO> branches = branchIds.stream()
                .map(id -> {
                    try {
                        return brancheService.findById(id);
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        notificationService.sendNotificationToBranches(notification, branches);
        return ResponseEntity.ok().build();
    }


    // ============================
    //    Excel File Management
    // ============================
    @PostMapping("/excel/upload")
    public ResponseEntity<Void> uploadExcelFile(@Valid @RequestBody ExcelUploadDTO uploadDTO) throws CustomException {
        excelFileService.upload(uploadDTO.getFile(), uploadDTO.getSeances());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/excel/history")
    public ResponseEntity<List<FichierExcelDTO>> getImportHistory() throws CustomException {
        return ResponseEntity.ok(excelFileService.getImportHistory());
    }
}
/**
 * Wrapper DTO for Excel file uploads
 */
@Data
class ExcelUploadDTO {
    private FichierExcelDTO file;
    private List<SeanceDTO> seances;
}