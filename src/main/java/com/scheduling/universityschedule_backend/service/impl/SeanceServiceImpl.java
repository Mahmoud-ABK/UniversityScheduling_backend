package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceConflictDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.SeanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for session management.
 * Handles CRUD operations and conflict detection.
 */
@Service
@Transactional
public class SeanceServiceImpl implements SeanceService {

    private final SeanceRepository seanceRepository;
    private final EntityMapper mapper;

    public SeanceServiceImpl(SeanceRepository seanceRepository, EntityMapper mapper) {
        this.seanceRepository = seanceRepository;
        this.mapper = mapper;
    }

    @Override
    public List<SeanceDTO> findAll() throws CustomException {
        try {
            return seanceRepository.findAll()
                    .stream()
                    .map(mapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve sessions: " + e.getMessage());
        }
    }

    @Override
    public SeanceDTO findById(Long id) throws CustomException {
        Optional<Seance> seanceOpt = seanceRepository.findById(id);
        if (seanceOpt.isEmpty()) {
            throw new CustomException("Session not found with id " + id);
        }
        return mapper.toSeanceDTO(seanceOpt.get());
    }

    @Override
    public SeanceDTO create(SeanceDTO seanceDTO) throws CustomException {
        try {
            Seance seance = mapper.toSeance(seanceDTO);
            seance = seanceRepository.save(seance);
            return mapper.toSeanceDTO(seance);
        } catch (Exception e) {
            throw new CustomException("Failed to create session: " + e.getMessage());
        }
    }

    @Override
    public SeanceDTO update(Long id, SeanceDTO seanceDTO) throws CustomException {
        Seance existing = seanceRepository.findById(id)
                .orElseThrow(() -> new CustomException("Session not found with id " + id));
        existing.setJour(seanceDTO.getJour());
        existing.setHeureDebut(seanceDTO.getHeureDebut());
        existing.setHeureFin(seanceDTO.getHeureFin());
        existing.setType(seanceDTO.getType());
        existing.setMatiere(seanceDTO.getMatiere());
        existing.setFrequence(seanceDTO.getFrequence());
        seanceRepository.save(existing);
        return mapper.toSeanceDTO(existing);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!seanceRepository.existsById(id)) {
            throw new CustomException("Session not found with id " + id);
        }
        seanceRepository.deleteById(id);
    }

    @Override
    public List<SeanceConflictDTO> getAllConflicts() throws CustomException {
        try {
            List<Object[]> conflictPairs = seanceRepository.findConflictingSeancePairs();
            List<SeanceConflictDTO> conflicts = new ArrayList<>();
            for (Object[] pair : conflictPairs) {
                Seance s1 = (Seance) pair[0];
                Seance s2 = (Seance) pair[1];
                SeanceConflictDTO conflict = new SeanceConflictDTO(s1.getId(), s2.getId(), List.of("general"));
                conflicts.add(conflict);
            }
            return conflicts;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve conflicts: " + e.getMessage());
        }
    }

    @Override
    public List<SeanceConflictDTO> getRoomConflicts() throws CustomException {
        try {
            List<Object[]> conflictPairs = seanceRepository.findConflictingByRooms();
            List<SeanceConflictDTO> conflicts = new ArrayList<>();
            for (Object[] pair : conflictPairs) {
                Seance s1 = (Seance) pair[0];
                Seance s2 = (Seance) pair[1];
                SeanceConflictDTO conflict = new SeanceConflictDTO(s1.getId(), s2.getId(), List.of("room"));
                conflicts.add(conflict);
            }
            return conflicts;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve room conflicts: " + e.getMessage());
        }
    }

    @Override
    public List<SeanceConflictDTO> getConflictsForSession(Long seanceId) throws CustomException {
        try {
            Seance target = seanceRepository.findById(seanceId)
                    .orElseThrow(() -> new CustomException("Session not found with id " + seanceId));
            List<Seance> conflicts = seanceRepository.findRoomConflictsForSeance(
                    seanceId, target.getJour(), 
                    target.getSalle() != null ? target.getSalle().getId() : null,
                    target.getHeureDebut(), target.getHeureFin());
            return conflicts.stream()
                    .map(s -> new SeanceConflictDTO(seanceId, s.getId(), List.of("room")))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve conflicts for session: " + e.getMessage());
        }
    }
}
