package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceConflictDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.SeanceRoomConflictDTO;
import com.scheduling.universityschedule_backend.dto.SingleSeanceConflictDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FrequenceType;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SeanceServiceImpl implements SeanceService {

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<SeanceDTO> findAll() throws CustomException {
        try {
            List<Seance> seances = seanceRepository.findAll();
            return seances.stream()
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all sessions", e);
        }
    }

    @Override
    public SeanceDTO findById(Long id) throws CustomException {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new CustomException("Session not found with ID: " + id));
        return entityMapper.toSeanceDTO(seance);
    }

    @Override
    public SeanceDTO create(SeanceDTO seanceDTO) throws CustomException {
        try {
            Seance seance = entityMapper.toSeance(seanceDTO);
            Seance savedSeance = seanceRepository.save(seance);
            return entityMapper.toSeanceDTO(savedSeance);
        } catch (Exception e) {
            throw new CustomException("Failed to create session", e);
        }
    }

    @Override
    public SeanceDTO update(Long id, SeanceDTO seanceDTO) throws CustomException {
        Seance existingSeance = seanceRepository.findById(id)
                .orElseThrow(() -> new CustomException("Session not found with ID: " + id));
        entityMapper.updateFromDto(seanceDTO, existingSeance);
        try {
            Seance updatedSeance = seanceRepository.save(existingSeance);
            return entityMapper.toSeanceDTO(updatedSeance);
        } catch (Exception e) {
            throw new CustomException("Failed to update session", e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!seanceRepository.existsById(id)) {
            throw new CustomException("Session not found with ID: " + id);
        }
        try {
            seanceRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException("Failed to delete session", e);
        }
    }

    @Override
    public List<SeanceConflictDTO> getAllConflicts() throws CustomException {
        try {
            List<Object[]> conflicts = seanceRepository.findConflictingSeancePairs(FrequenceType.BIWEEKLY,FrequenceType.CATCHUP);
            return entityMapper.toSeanceConflictDTOList(conflicts);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all session conflicts", e);
        }
    }

    @Override
    public List<SeanceRoomConflictDTO> getRoomConflicts() throws CustomException {
        try {
            List<Object[]> roomConflicts = seanceRepository.findConflictingByRooms(FrequenceType.BIWEEKLY,FrequenceType.CATCHUP);
            return entityMapper.toSeanceRoomConflictDTOList(roomConflicts);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve room conflicts", e);
        }
    }

    @Override
    public List<SingleSeanceConflictDTO> getConflictsForSession(Long seanceId) throws CustomException {
        try {
            List<Object[]> conflicts = seanceRepository.findRoomConflictsForSeance(seanceId,FrequenceType.BIWEEKLY,FrequenceType.CATCHUP);
            return entityMapper.toSingleSeanceConflictDTOList(conflicts);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve conflicts for session", e);
        }
    }
}