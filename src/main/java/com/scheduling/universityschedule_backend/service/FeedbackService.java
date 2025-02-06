package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Signal;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    // PropositionDeRattrapage operations
    PropositionDeRattrapage createProposition(PropositionDeRattrapage proposition);
    Optional<PropositionDeRattrapage> getPropositionById(Long id);
    List<PropositionDeRattrapage> getAllPropositions();
    PropositionDeRattrapage updateProposition(PropositionDeRattrapage proposition);
    void deleteProposition(Long id);

    // Signal operations
    Signal createSignal(Signal signal);
    Optional<Signal> getSignalById(Long id);
    List<Signal> getAllSignals();
    Signal updateSignal(Signal signal);
    void deleteSignal(Long id);
}
