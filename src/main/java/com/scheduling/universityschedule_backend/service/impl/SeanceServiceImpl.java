package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceConflictDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeanceServiceImpl implements SeanceService {

    private final SeanceRepository seanceRepository;
    private final EntityMapper mapper;

    @Autowired
    public SeanceServiceImpl(SeanceRepository seanceRepository, EntityMapper mapper) {
        this.seanceRepository = seanceRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all sessions.
     */
    @Override
    public List<SeanceDTO> getAllSeances() throws CustomException {
        List<Seance> seances = seanceRepository.findAll();
        List<SeanceDTO> dtoList = new ArrayList<>();
        seances.forEach(s -> dtoList.add(mapper.toSeanceDTO(s)));
        return dtoList;
    }

    /**
     * Retrieves a session by its ID.
     */
    @Override
    public SeanceDTO getSeanceById(Long id) throws CustomException {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new CustomException("Seance not found"));
        return mapper.toSeanceDTO(seance);
    }

    /**
     * Creates a new session.
     */
    @Override
    public SeanceDTO createSeance(SeanceDTO seanceDTO) throws CustomException {
        try {
            Seance seance = mapper.toSeance(seanceDTO);
            seance = seanceRepository.save(seance);
            return mapper.toSeanceDTO(seance);
        } catch (Exception e) {
            throw new CustomException("Failed to create Seance", e);
        }
    }

    /**
     * Updates an existing session.
     */
    @Override
    public SeanceDTO updateSeance(Long id, SeanceDTO seanceDTO) throws CustomException {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new CustomException("Seance not found"));
        seance.setJour(seanceDTO.getJour());
        seance.setHeureDebut(seanceDTO.getHeureDebut());
        seance.setHeureFin(seanceDTO.getHeureFin());
        seance.setType(seanceDTO.getType());
        seance.setMatiere(seanceDTO.getMatiere());
        seance.setFrequence(seanceDTO.getFrequence());
        seance = seanceRepository.save(seance);
        return mapper.toSeanceDTO(seance);
    }

    /**
     * Deletes a session by its ID.
     */
    @Override
    public void deleteSeance(Long id) throws CustomException {
        if (!seanceRepository.existsById(id)) {
            throw new CustomException("Seance not found");
        }
        seanceRepository.deleteById(id);
    }

    /**
     * Detects conflicts for a given session. For simplicity, only room conflicts are checked.
     */
    @Override
    public List<SeanceConflictDTO> detectConflicts(Long seanceId) throws CustomException {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new CustomException("Seance not found"));
        // Here we call a custom repository query to check for room conflicts.
        List<Seance> conflictingSeances = seanceRepository.findRoomConflictsForSeance(
                seance.getId(),
                seance.getJour(),
                seance.getSalle() != null ? seance.getSalle().getId() : null,
                seance.getHeureDebut(),
                seance.getHeureFin()
        );
        List<SeanceConflictDTO> conflictDTOList = new ArrayList<>();
        for (Seance conflicting : conflictingSeances) {
            SeanceConflictDTO conflictDTO = new SeanceConflictDTO();
            conflictDTO.setSeance1(mapper.toSeanceDTO(seance));
            conflictDTO.setSeance2(mapper.toSeanceDTO(conflicting));
            List<String> conflictTypes = new ArrayList<>();
            conflictTypes.add("Room Conflict");
            conflictDTO.setConflictTypes(conflictTypes);
            conflictDTOList.add(conflictDTO);
        }
        return conflictDTOList;
    }
}
