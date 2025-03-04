package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EnseignantDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Enseignant;
import com.scheduling.universityschedule_backend.repository.EnseignantRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public EnseignantDTO findById(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));
        return entityMapper.toEnseignantDTO(enseignant);
    }

    @Override
    public List<EnseignantDTO> findAll() throws CustomException {
        List<Enseignant> enseignants = enseignantRepository.findAll();
        return enseignants.stream()
                .map(entityMapper::toEnseignantDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnseignantDTO create(EnseignantDTO enseignantDTO) throws CustomException {
        Enseignant enseignant = entityMapper.toEnseignant(enseignantDTO);
        Enseignant savedEnseignant = enseignantRepository.save(enseignant);
        return entityMapper.toEnseignantDTO(savedEnseignant);
    }

    @Override
    public EnseignantDTO update(Long id, EnseignantDTO enseignantDTO) throws CustomException {
        Enseignant existingEnseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));
        entityMapper.updateFromDto(enseignantDTO, existingEnseignant);
        Enseignant updatedEnseignant = enseignantRepository.save(existingEnseignant);
        return entityMapper.toEnseignantDTO(updatedEnseignant);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!enseignantRepository.existsById(id)) {
            throw new CustomException("Teacher not found with ID: " + id);
        }
        enseignantRepository.deleteById(id);
    }

    @Override
    public List<SeanceDTO> getSchedule(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));
        return enseignant.getSeances().stream()
                .map(entityMapper::toSeanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalTeachingHours(Long teacherId, LocalDate startDate, LocalDate endDate) throws CustomException {
        // Implement the logic to calculate total teaching hours
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public PropositionDeRattrapageDTO submitMakeupRequest(Long id, PropositionDeRattrapageDTO proposition) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));
        // Implement the logic to submit a makeup session request
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public SignalDTO submitSignal(Long id, SignalDTO signal) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));
        // Implement the logic to submit a signal
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public List<SignalDTO> getSignals(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));
        return enseignant.getSignals().stream()
                .map(entityMapper::toSignalDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getSubjects(Long id) throws CustomException {
        // Implement the logic to get all subjects taught by the teacher
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public List<TPDTO> getStudentGroups(Long id) throws CustomException {
        // Implement the logic to get all student groups taught by the teacher
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}