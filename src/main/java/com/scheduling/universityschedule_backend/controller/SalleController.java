package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.service.SalleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * REST Controller for room management operations.
 * Handles endpoints related to room management by technicians.
 *
 * @author Mahmoud-ABK
 * @version 1.0
 * @since 2025-05-04
 */
@RestController
@RequestMapping("/api/rooms")
public class SalleController {

    private final SalleService salleService;

    @Autowired
    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }

    // ============================
    //    Room Management
    // ============================

    @GetMapping
    public ResponseEntity<List<SalleDTO>> getAllRooms() throws CustomException {
        return ResponseEntity.ok(salleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleDTO> getRoomById(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(salleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SalleDTO> createRoom(@RequestBody SalleDTO salleDTO) throws CustomException {
        return ResponseEntity.ok(salleService.create(salleDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalleDTO> updateRoom(
            @PathVariable Long id,
            @RequestBody SalleDTO salleDTO) throws CustomException {
        return ResponseEntity.ok(salleService.update(id, salleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) throws CustomException {
        salleService.delete(id);
        return ResponseEntity.ok().build();
    }
}