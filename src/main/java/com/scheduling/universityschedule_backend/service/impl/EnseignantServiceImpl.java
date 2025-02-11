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
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.repository.SignalRepository;
import com.scheduling.universityschedule_backend.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final PropositionDeRattrapageRepository propositionRepo;
    private final SignalRepository signalRepository;
    private final SeanceRepository seanceRepository;
    private final EntityMapper mapper;

    @Autowired
    public EnseignantServiceImpl(EnseignantRepository enseignantRepository,
                                 PropositionDeRattrapageRepository propositionRepo,
                                 SignalRepository signalRepository,
                                 SeanceRepository seanceRepository,
                                 EntityMapper mapper) {
        this.enseignantRepository = enseignantRepository;
        this.propositionRepo = propositionRepo;
        this.signalRepository = signalRepository;
        this.seanceRepository = seanceRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves the teaching schedule (seances) for a given teacher.
     */
    @Override
    public List<SeanceDTO> getEmploiDuTemps(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found"));
        List<SeanceDTO> seanceDTOList = new ArrayList<>();
        if (enseignant.getSeances() != null) {
            for (Seance s : enseignant.getSeances()) {
                seanceDTOList.add(mapper.toSeanceDTO(s));
            }
        }
        return seanceDTOList;
    }

    /**
     * Retrieves the total teaching hours for a teacher.
     */
    @Override
    public int getHeuresEnseignees(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found"));
        return enseignant.getHeures();
    }

    /**
     * Submits a catch-up session proposal.
     */
    @Override
    public PropositionDeRattrapageDTO soumettreDemandeRattrapage(Long id, PropositionDeRattrapageDTO propositionDTO) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found"));
        PropositionDeRattrapage proposal = mapper.toPropositionDeRattrapage(propositionDTO);
        proposal.setEnseignant(enseignant);
        proposal.setStatus("pending");
        proposal.setDate(LocalDateTime.now());
        proposal = propositionRepo.save(proposal);
        return mapper.toPropositionDeRattrapageDTO(proposal);
    }

    /**
     * Submits a suggestion (signal).
     */
    @Override
    public SignalDTO soumettreSuggestion(Long id, SignalDTO signalDTO) throws CustomException {
        // Note: In a full implementation, the teacher id would be used to associate the signal.
        Signal signal = mapper.toSignal(signalDTO);
        signal.setTimestamp(LocalDateTime.now());
        signal = signalRepository.save(signal);
        return mapper.toSignalDTO(signal);
    }

    /**
     * Retrieves signals (suggestions).
     */
    @Override
    public List<SignalDTO> getSignalisations(Long id) throws CustomException {
        List<Signal> signals = signalRepository.findAll();
        List<SignalDTO> dtoList = new ArrayList<>();
        for (Signal signal : signals) {
            dtoList.add(mapper.toSignalDTO(signal));
        }
        return dtoList;
    }
}
