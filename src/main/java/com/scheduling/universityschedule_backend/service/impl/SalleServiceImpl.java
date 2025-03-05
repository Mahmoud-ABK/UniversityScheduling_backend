package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Salle;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.service.SalleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for room management.
 * Handles room creation, updates, and availability checking.
 */
@Service
@Transactional
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     */
    public SalleServiceImpl(SalleRepository salleRepository,
                            EntityMapper entityMapper) {
        this.salleRepository = salleRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public SalleDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Room ID cannot be null");
            }

            // Retrieve room
            Salle salle = salleRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Room not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toSalleDTO(salle);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve room with ID: " + id, e);
        }
    }

    @Override
    public List<SalleDTO> findAll() throws CustomException {
        try {
            // Retrieve all rooms
            List<Salle> salles = salleRepository.findAll();

            // Convert to DTOs
            return salles.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSalleDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all rooms", e);
        }
    }

    @Override
    public SalleDTO create(SalleDTO salleDTO) throws CustomException {
        try {
            // Validate input
            if (salleDTO == null) {
                throw new CustomException("Room data cannot be null");
            }

            // Check for duplicate ID if provided
            if (salleDTO.getId() != null && salleRepository.existsById(salleDTO.getId())) {
                throw new CustomException("Room with ID " + salleDTO.getId() + " already exists");
            }

            // Convert to entity
            Salle salle = entityMapper.toSalle(salleDTO);

            // Save entity
            Salle savedSalle = salleRepository.save(salle);

            // Convert back to DTO
            return entityMapper.toSalleDTO(savedSalle);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create room", e);
        }
    }

    @Override
    public SalleDTO update(Long id, SalleDTO salleDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Room ID cannot be null");
            }

            if (salleDTO == null) {
                throw new CustomException("Room data cannot be null");
            }

            // Find existing room
            Salle existingSalle = salleRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Room not found with ID: " + id));

            // Update entity with DTO data
            entityMapper.updateFromDto(salleDTO, existingSalle);

            // Save updated entity
            Salle updatedSalle = salleRepository.save(existingSalle);

            // Convert back to DTO
            return entityMapper.toSalleDTO(updatedSalle);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update room with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Room ID cannot be null");
            }

            // Check if room exists
            if (!salleRepository.existsById(id)) {
                throw new CustomException("Room not found with ID: " + id);
            }

            // Delete room
            salleRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete room with ID: " + id, e);
        }
    }

    @Override
    public List<SalleDTO> getAvailableRooms(LocalDate date, DayOfWeek day, LocalTime startTime, LocalTime endTime) throws CustomException {
        try {
            // Input validation
            if (day == null) {
                throw new CustomException("Day of week cannot be null");
            }
            if (startTime == null || endTime == null) {
                throw new CustomException("Start time and end time cannot be null");
            }
            if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
                throw new CustomException("Start time must be before end time");
            }

            // For catch-up sessions (which require a specific date)
            // the date parameter is optional for regular sessions

            // Use the repository's custom query method that already handles the logic efficiently
            List<Salle> availableRooms = salleRepository.findAvailableRooms(day, startTime, endTime, date);

            // Transform to DTOs
            return availableRooms.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSalleDTO)
                    .collect(Collectors.toList());

        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date/time format: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CustomException("Failed to find available rooms: " + e.getMessage(), e);
        }
    }
}