package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.exception.CustomException;
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
    public FeedbackServiceImpl(PropositionDeRattrapageRepository propositionRepository,
                               SignalRepository signalRepository) {
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
    public PropositionDeRattrapage updateProposition(PropositionDeRattrapage proposition) throws CustomException {
        if (proposition.getId() == null || !propositionRepository.existsById(proposition.getId())) {
            throw new CustomException("Proposition not found with id: " + proposition.getId());
        }
        return propositionRepository.save(proposition);
    }

    @Override
    public void deleteProposition(Long id) throws CustomException {
        if (!propositionRepository.existsById(id)) {
            throw new CustomException("Proposition not found with id: " + id);
        }
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
    public Signal updateSignal(Signal signal) throws CustomException {
        if (signal.getId() == null || !signalRepository.existsById(signal.getId())) {
            throw new CustomException("Signal not found with id: " + signal.getId());
        }
        return signalRepository.save(signal);
    }

    @Override
    public void deleteSignal(Long id) throws CustomException {
        if (!signalRepository.existsById(id)) {
            throw new CustomException("Signal not found with id: " + id);
        }
        signalRepository.deleteById(id);
    }

    // New Methods (for admin review actions):

    @Override
    public void resolveFeedback(Long signalId, String resolution) throws CustomException {
        Signal signal = signalRepository.findById(signalId)
                .orElseThrow(() -> new CustomException("Signal not found with id: " + signalId));
        // Mark the signal as resolved and store the resolution message.
        signalRepository.save(signal);
    }

    @Override
    public void approveCatchUpProposal(Long proposalId) throws CustomException {
        PropositionDeRattrapage proposition = propositionRepository.findById(proposalId)
                .orElseThrow(() -> new CustomException("Proposition not found with id: " + proposalId));
        // Set the status to "approved".
        proposition.setStatus("approved");
        propositionRepository.save(proposition);
    }

    @Override
    public void rejectCatchUpProposal(Long proposalId, String rejectionReason) throws CustomException {
        PropositionDeRattrapage proposition = propositionRepository.findById(proposalId)
                .orElseThrow(() -> new CustomException("Proposition not found with id: " + proposalId));
        // Set the status to "rejected" and record the rejection reason.
        proposition.setStatus("rejected");
        proposition.setReason(rejectionReason);
        propositionRepository.save(proposition);
    }
}
