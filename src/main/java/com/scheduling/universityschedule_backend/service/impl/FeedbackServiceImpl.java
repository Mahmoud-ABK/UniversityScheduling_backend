package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Signal;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.repository.SignalRepository;
import com.scheduling.universityschedule_backend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final PropositionDeRattrapageRepository propositionRepository;
    private final SignalRepository signalRepository;

    @Autowired
    public FeedbackServiceImpl(PropositionDeRattrapageRepository propositionRepository, SignalRepository signalRepository) {
        this.propositionRepository = propositionRepository;
        this.signalRepository = signalRepository;
    }

    // PropositionDeRattrapage operations
    @Override
    public PropositionDeRattrapage createProposition(PropositionDeRattrapage proposition) {
        return propositionRepository.save(proposition);
    }

    @Override
    public Optional<PropositionDeRattrapage> getPropositionById(Long id) {
        return propositionRepository.findById(id);
    }

    @Override
    public List<PropositionDeRattrapage> getAllPropositions() {
        return propositionRepository.findAll();
    }

    @Override
    public PropositionDeRattrapage updateProposition(PropositionDeRattrapage proposition) {
        return propositionRepository.save(proposition);
    }

    @Override
    public void deleteProposition(Long id) {
        propositionRepository.deleteById(id);
    }

    // Signal operations
    @Override
    public Signal createSignal(Signal signal) {
        return signalRepository.save(signal);
    }

    @Override
    public Optional<Signal> getSignalById(Long id) {
        return signalRepository.findById(id);
    }

    @Override
    public List<Signal> getAllSignals() {
        return signalRepository.findAll();
    }

    @Override
    public Signal updateSignal(Signal signal) {
        return signalRepository.save(signal);
    }

    @Override
    public void deleteSignal(Long id) {
        signalRepository.deleteById(id);
    }
}
