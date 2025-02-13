package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Salle;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.service.SalleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for room management.
 * Handles CRUD operations and room availability.
 */
@Service
@Transactional
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;
    private final EntityMapper mapper;

    public SalleServiceImpl(SalleRepository salleRepository, EntityMapper mapper) {
        this.salleRepository = salleRepository;
        this.mapper = mapper;
    }

    @Override
    public List<SalleDTO> findAll() throws CustomException {
        try {
            return salleRepository.findAll()
                    .stream()
                    .map(mapper::toSalleDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve rooms: " + e.getMessage());
        }
    }

    @Override
    public SalleDTO findById(Long id) throws CustomException {
        Optional<Salle> salleOpt = salleRepository.findById(id);
        if (salleOpt.isEmpty()) {
            throw new CustomException("Room not found with id " + id);
        }
        return mapper.toSalleDTO(salleOpt.get());
    }

    @Override
    public SalleDTO create(SalleDTO salleDTO) throws CustomException {
        try {
            Salle salle = mapper.toSalle(salleDTO);
            salle = salleRepository.save(salle);
            return mapper.toSalleDTO(salle);
        } catch (Exception e) {
            throw new CustomException("Failed to create room: " + e.getMessage());
        }
    }

    @Override
    public SalleDTO update(Long id, SalleDTO salleDTO) throws CustomException {
        Salle existing = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Room not found with id " + id));
        existing.setIdentifiant(salleDTO.getIdentifiant());
        existing.setType(salleDTO.getType());
        existing.setCapacite(salleDTO.getCapacite());
        existing.setDisponibilite(salleDTO.getDisponibilite());
        salleRepository.save(existing);
        return mapper.toSalleDTO(existing);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!salleRepository.existsById(id)) {
            throw new CustomException("Room not found with id " + id);
        }
        salleRepository.deleteById(id);
    }

    @Override
    public List<SalleDTO> getAvailableRooms(String date, String startTime, String endTime) throws CustomException {
        try {
            return salleRepository.findAll().stream()
                    .filter(salle -> salle.getDisponibilite() != null &&
                            salle.getDisponibilite().stream().anyMatch(slot -> slot.contains(date) &&
                                    slot.contains(startTime) && slot.contains(endTime)))
                    .map(mapper::toSalleDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to search available rooms: " + e.getMessage());
        }
    }
}
