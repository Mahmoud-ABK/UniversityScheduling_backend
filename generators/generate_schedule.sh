#!/bin/bash

# Define the path where the controller file should be created
CONTROLLER_PATH="src/main/java/com/scheduling/universityschedule_backend/controller"

# Create the controller folder if it doesn't exist
mkdir -p "$CONTROLLER_PATH"

# Create the ScheduleController.java file with the required package and content
cat <<EOL > "$CONTROLLER_PATH/ScheduleController.java"
package com.scheduling.universityschedule_backend.controller;

import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.TDDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.service.ScheduleService;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.model.TP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // **Admin-only functionalities**
    
    // Admin can create a Seance (session)
    @PostMapping("/seance")
    public ResponseEntity<SeanceDTO> createSeance(@RequestBody SeanceDTO seanceDTO) {
        SeanceDTO createdSeance = scheduleService.createSeance(seanceDTO);
        return ResponseEntity.ok(createdSeance);
    }

    // Admin can update a Seance (session)
    @PutMapping("/seance/{id}")
    public ResponseEntity<SeanceDTO> updateSeance(@PathVariable Long id, @RequestBody SeanceDTO seanceDTO) {
        SeanceDTO updatedSeance = scheduleService.updateSeance(id, seanceDTO);
        return ResponseEntity.ok(updatedSeance);
    }

    // Admin can delete a Seance (session)
    @DeleteMapping("/seance/{id}")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        scheduleService.deleteSeance(id);
        return ResponseEntity.noContent().build();
    }

    // Admin can create a TD (Tutorial session)
    @PostMapping("/td")
    public ResponseEntity<TDDTO> createTD(@RequestBody TDDTO tdDTO) {
        TDDTO createdTD = scheduleService.createTD(tdDTO);
        return ResponseEntity.ok(createdTD);
    }

    // Admin can update a TD (Tutorial session)
    @PutMapping("/td/{id}")
    public ResponseEntity<TDDTO> updateTD(@PathVariable Long id, @RequestBody TDDTO tdDTO) {
        TDDTO updatedTD = scheduleService.updateTD(id, tdDTO);
        return ResponseEntity.ok(updatedTD);
    }

    // Admin can delete a TD (Tutorial session)
    @DeleteMapping("/td/{id}")
    public ResponseEntity<Void> deleteTD(@PathVariable Long id) {
        scheduleService.deleteTD(id);
        return ResponseEntity.noContent().build();
    }

    // Admin can create a TP (Practical session)
    @PostMapping("/tp")
    public ResponseEntity<TPDTO> createTP(@RequestBody TPDTO tpDTO) {
        TPDTO createdTP = scheduleService.createTP(tpDTO);
        return ResponseEntity.ok(createdTP);
    }

    // Admin can update a TP (Practical session)
    @PutMapping("/tp/{id}")
    public ResponseEntity<TPDTO> updateTP(@PathVariable Long id, @RequestBody TPDTO tpDTO) {
        TPDTO updatedTP = scheduleService.updateTP(id, tpDTO);
        return ResponseEntity.ok(updatedTP);
    }

    // Admin can delete a TP (Practical session)
    @DeleteMapping("/tp/{id}")
    public ResponseEntity<Void> deleteTP(@PathVariable Long id) {
        scheduleService.deleteTP(id);
        return ResponseEntity.noContent().build();
    }

    // **Technician-only functionalities**

    // Technician can manage rooms (Salles)
    // Technician can create a Room (Salle)
    @PostMapping("/room")
    public ResponseEntity<Void> createRoom(@RequestBody Salle salle) {
        scheduleService.createSalle(salle);
        return ResponseEntity.ok().build();
    }

    // Technician can update a Room (Salle)
    @PutMapping("/room/{id}")
    public ResponseEntity<Void> updateRoom(@PathVariable Long id, @RequestBody Salle salle) {
        scheduleService.updateSalle(salle);
        return ResponseEntity.ok().build();
    }

    // Technician can delete a Room (Salle)
    @DeleteMapping("/room/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        scheduleService.deleteSalle(id);
        return ResponseEntity.noContent().build();
    }

    // Technician can view all rooms (Salle)
    @GetMapping("/rooms")
    public ResponseEntity<List<Salle>> getAllRooms() {
        List<Salle> rooms = scheduleService.getAllSalles();
        return ResponseEntity.ok(rooms);
    }

    // **Teacher-only functionalities**

    // Teacher can view all their Seances (sessions)
    @GetMapping("/seances/teacher/{teacherId}")
    public ResponseEntity<List<SeanceDTO>> getSeancesForTeacher(@PathVariable Long teacherId) {
        List<SeanceDTO> seances = scheduleService.getSeancesForTeacher(teacherId);
        return ResponseEntity.ok(seances);
    }

    // Teacher can view their teaching hours
    @GetMapping("/teaching-hours/teacher/{teacherId}")
    public ResponseEntity<Integer> getTeachingHoursForTeacher(@PathVariable Long teacherId) {
        int teachingHours = scheduleService.getTeachingHoursForTeacher(teacherId);
        return ResponseEntity.ok(teachingHours);
    }

    // Teacher can view all the catch-up sessions (proposals)
    @GetMapping("/catch-up-proposals/teacher/{teacherId}")
    public ResponseEntity<List<SeanceDTO>> getCatchUpProposalsForTeacher(@PathVariable Long teacherId) {
        List<SeanceDTO> catchUpProposals = scheduleService.getCatchUpProposalsForTeacher(teacherId);
        return ResponseEntity.ok(catchUpProposals);
    }

    // **Student-only functionalities**

    // Student can view their Seances (sessions)
    @GetMapping("/seances/student/{studentId}")
    public ResponseEntity<List<SeanceDTO>> getSeancesForStudent(@PathVariable Long studentId) {
        List<SeanceDTO> seances = scheduleService.getSeancesForStudent(studentId);
        return ResponseEntity.ok(seances);
    }

    // Student can view their tutorial sessions (TD)
    @GetMapping("/tds/student/{studentId}")
    public ResponseEntity<List<TDDTO>> getTDsForStudent(@PathVariable Long studentId) {
        List<TDDTO> tds = scheduleService.getTDsForStudent(studentId);
        return ResponseEntity.ok(tds);
    }

    // Student can view their practical sessions (TP)
    @GetMapping("/tps/student/{studentId}")
    public ResponseEntity<List<TPDTO>> getTPsForStudent(@PathVariable Long studentId) {
        List<TPDTO> tps = scheduleService.getTPsForStudent(studentId);
        return ResponseEntity.ok(tps);
    }

    // **Common functionalities** (accessible by Admin, Teacher, Student, and Technician)

    // Anyone can view all Seances (sessions) (e.g., for general timetable view)
    @GetMapping("/seances")
    public ResponseEntity<List<SeanceDTO>> getAllSeances() {
        List<SeanceDTO> allSeances = scheduleService.getAllSeances();
        return ResponseEntity.ok(allSeances);
    }

    // Anyone can view all TD sessions (tutorials)
    @GetMapping("/tds")
    public ResponseEntity<List<TDDTO>> getAllTDs() {
        List<TDDTO> allTDs = scheduleService.getAllTDs();
        return ResponseEntity.ok(allTDs);
    }

    // Anyone can view all TP sessions (practicals)
    @GetMapping("/tps")
    public ResponseEntity<List<TPDTO>> getAllTPs() {
        List<TPDTO> allTPs = scheduleService.getAllTPs();
        return ResponseEntity.ok(allTPs);
    }
}
EOL

# Print success message
echo "ScheduleController.java has been successfully created at $CONTROLLER_PATH"
