package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EnseignantDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Enseignant;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.Signal;
import com.scheduling.universityschedule_backend.repository.EnseignantRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.repository.SignalRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.service.EnseignantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for teacher operations.
 * Manages teacher schedules, teaching hours, makeup requests, and signals.
 */
@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final SeanceRepository seanceRepository;
    private final SignalRepository signalRepository;
    private final PropositionDeRattrapageRepository propositionRepository;
    private final EntityMapper mapper;

    public EnseignantServiceImpl(EnseignantRepository enseignantRepository,
                                 SeanceRepository seanceRepository,
                                 SignalRepository signalRepository,
                                 PropositionDeRattrapageRepository propositionRepository,
                                 EntityMapper mapper) {
        this.enseignantRepository = enseignantRepository;
        this.seanceRepository = seanceRepository;
        this.signalRepository = signalRepository;
        this.propositionRepository = propositionRepository;
        this.mapper = mapper;
    }

    @Override
    public EnseignantDTO findById(Long id) throws CustomException {
        Optional<Enseignant> enseignantOpt = enseignantRepository.findById(id);
        if (enseignantOpt.isEmpty()) {
            throw new CustomException("Enseignant not found with id " + id);
        }
        return mapper.toEnseignantDTO(enseignantOpt.get());
    }

    @Override
    public List<SeanceDTO> getSchedule(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found with id " + id));
        List<Seance> sessions = enseignant.getSeances();
        return sessions.stream().map(mapper::toSeanceDTO).collect(Collectors.toList());
    }

    @Override
    public int getTotalTeachingHours(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found with id " + id));
        return enseignant.getHeures();
    }

    @Override
    public PropositionDeRattrapageDTO submitMakeupRequest(Long id, PropositionDeRattrapageDTO propositionDTO) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found with id " + id));
        PropositionDeRattrapage proposition = mapper.toPropositionDeRattrapage(propositionDTO);
        proposition.setEnseignant(enseignant);
        proposition.setStatus("pending");
        proposition = propositionRepository.save(proposition);
        return mapper.toPropositionDeRattrapageDTO(proposition);
    }

    @Override
    public SignalDTO submitSignal(Long id, SignalDTO signalDTO) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found with id " + id));
        Signal signal = mapper.toSignal(signalDTO);
        signal = signalRepository.save(signal);
        return mapper.toSignalDTO(signal);
    }

    @Override
    public List<SignalDTO> getSignals(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found with id " + id));
        // In a real application, filter signals by teacher.
        List<Signal> allSignals = signalRepository.findAll();
        return allSignals.stream()
                .filter(signal -> signal.getId() != null) // Replace with actual teacher filter if available.
                .map(mapper::toSignalDTO)
                .collect(Collectors.toList());
    }
}
