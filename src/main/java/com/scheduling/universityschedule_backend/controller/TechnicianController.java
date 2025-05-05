package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.TechnicienDTO;
import com.scheduling.universityschedule_backend.dto.UserDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.service.TechnicienService;
import com.scheduling.universityschedule_backend.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@PreAuthorize("hasAuthority('ROLE_TECHNICIAN')")
public class TechnicianController {

    private final TechnicienService technicienService;
    private final UserManagementService userManagementService;

    public TechnicianController(TechnicienService technicienService, UserManagementService userManagementService) {
        this.technicienService = technicienService;
        this.userManagementService = userManagementService;
    }

    // ============================
    // Technician Management Endpoints
    // ============================

    @GetMapping("/{id}")
    public ResponseEntity<TechnicienDTO> findTechnicianById(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(technicienService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TechnicienDTO>> findAllTechnicians() throws CustomException {
        return ResponseEntity.ok(technicienService.findAll());
    }

    @PostMapping
    public ResponseEntity<TechnicienDTO> createTechnician(@Valid @RequestBody TechnicienDTO technicienDTO) throws CustomException {
        return ResponseEntity.status(HttpStatus.CREATED).body(technicienService.create(technicienDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicienDTO> updateTechnician(@PathVariable Long id, @Valid @RequestBody TechnicienDTO technicienDTO) throws CustomException {
        return ResponseEntity.ok(technicienService.update(id, technicienDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable Long id) throws CustomException {
        technicienService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ============================
    // User Management Endpoints
    // ============================

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) throws CustomException {
        return ResponseEntity.ok(userManagementService.findById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAllUsers() throws CustomException {
        return ResponseEntity.ok(userManagementService.findAll());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws CustomException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userManagementService.create(userDTO));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) throws CustomException {
        return ResponseEntity.ok(userManagementService.update(id, userDTO));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws CustomException {
        userManagementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/personne/{personneId}")
    public ResponseEntity<UserDTO> findUserByPersonneId(@PathVariable Long personneId) throws CustomException {
        return ResponseEntity.ok(userManagementService.findByPersonneId(personneId));
    }
}