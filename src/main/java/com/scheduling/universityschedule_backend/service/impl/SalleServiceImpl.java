package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalleServiceImpl implements SalleService {

    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public SalleDTO findById(Long id) throws CustomException {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Room not found with ID: " + id));
        return entityMapper.toSalleDTO(salle);
    }

    @Override
    public List<SalleDTO> findAll() throws CustomException {
        List<Salle> salles = salleRepository.findAll();
        return salles.stream()
                .map(entityMapper::toSalleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SalleDTO create(SalleDTO salleDTO) throws CustomException {
        Salle salle = entityMapper.toSalle(salleDTO);
        Salle savedSalle = salleRepository.save(salle);
        return entityMapper.toSalleDTO(savedSalle);
    }

    @Override
    public SalleDTO update(Long id, SalleDTO salleDTO) throws CustomException {
        Salle existingSalle = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Room not found with ID: " + id));
        entityMapper.updateFromDto(salleDTO, existingSalle);
        Salle updatedSalle = salleRepository.save(existingSalle);
        return entityMapper.toSalleDTO(updatedSalle);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!salleRepository.existsById(id)) {
            throw new CustomException("Room not found with ID: " + id);
        }
        salleRepository.deleteById(id);
    }

    @Override
    public List<SalleDTO> getAvailableRooms(String date, String day, String startTime, String endTime) throws CustomException {
       try {
//            // 1. Get all rooms
//            List<Salle> allRooms = salleRepository.findAll();
//
//            // 2. Parse input times with formatters
//            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//            LocalTime requestedStartTime = LocalTime.parse(startTime, timeFormatter);
//            LocalTime requestedEndTime = LocalTime.parse(endTime, timeFormatter);
//            LocalDate requestedDate = LocalDate.parse(date, dateFormatter);
//
//            // 3. Get all sessions that might conflict with the requested time slot
//            List<Seance> conflictingSeances = seanceRepository.findAll().stream()
//                    .filter(seance -> {
//                        // Filter by day of week
//                        if (seance.getJour() != null && seance.getJour().equalsIgnoreCase(day)) {
//                            try {
//                                // Parse seance times
//                                LocalTime seanceStartTime = LocalTime.parse(seance.getHeureDebut(), timeFormatter);
//                                LocalTime seanceEndTime = LocalTime.parse(seance.getHeureFin(), timeFormatter);
//
//                                // Check if times overlap
//                                boolean timesOverlap = isTimeOverlapping(requestedStartTime, requestedEndTime, seanceStartTime, seanceEndTime);
//
//                                if (!timesOverlap) {
//                                    return false; // No time overlap, no conflict
//                                }
//
//                                // Check frequency
//                                String frequency = seance.getFrequence();
//                                if (frequency == null || frequency.isEmpty() || frequency.equals("1/15")) {
//                                    // Weekly or default frequency - always conflicts on the same day
//                                    return true;
//                                } else {
//                                    // For specific dates, check if it's the requested date
//                                    // Attempting to parse frequency as a date - will throw exception if not valid
//                                    LocalDate sessionDate = LocalDate.parse(frequency, dateFormatter);
//                                    return requestedDate.equals(sessionDate);
//                                }
//                            } catch (DateTimeParseException e) {
//                                // Let the upper function handle this
//                                throw new RuntimeException("Error parsing date/time in seance ID: " +
//                                        seance.getId() + ": " + e.getMessage(), e);
//                            }
//                        }
//                        return false;
//                    })
//                    .toList();
//
//            // 4. Extract IDs of rooms that are occupied during the requested time slot
//            Set<Long> occupiedRoomIds = conflictingSeances.stream()
//                    .filter(seance -> seance.getSalle() != null)
//                    .map(seance -> seance.getSalle().getId())
//                    .collect(Collectors.toSet());
//
//            // 5. Filter out occupied rooms and get available ones
//            List<Salle> availableRooms = allRooms.stream()
//                    .filter(room -> !occupiedRoomIds.contains(room.getId()))
//                    .toList();
//
//            // 6. Convert to DTOs and return
//            return availableRooms.stream()
//                    .map(entityMapper::toSalleDTO)
//                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            // Convert to custom exception with clear message and rethrow
            throw new CustomException("Invalid date/time format: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            // Capture and rethrow any runtime exceptions from the stream
            throw new CustomException("Error during room availability check: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CustomException("Failed to find available rooms: " + e.getMessage(), e);
        }
        return List.of();
    }

    /**
     * Helper method to determine if two time ranges overlap using Java's LocalTime
     *
     * @param start1 Start time of first range
     * @param end1   End time of first range
     * @param start2 Start time of second range
     * @param end2   End time of second range
     * @return true if the time ranges overlap
     */
    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}