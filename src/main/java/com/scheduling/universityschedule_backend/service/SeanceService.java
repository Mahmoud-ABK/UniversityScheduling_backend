package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing class sessions
 */
public interface SeanceService {
    List<SeanceDTO> getAllSeances() throws CustomException;
    SeanceDTO getSeanceById(Long id) throws CustomException;
    SeanceDTO createSeance(SeanceDTO seanceDTO) throws CustomException;
    SeanceDTO updateSeance(Long id, SeanceDTO seanceDTO) throws CustomException;
    void deleteSeance(Long id) throws CustomException;
    List<SeanceConflictDTO> detectConflicts(Long seanceId) throws CustomException;
}
