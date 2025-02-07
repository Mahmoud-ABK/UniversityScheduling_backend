package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Signal;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    // PropositionDeRattrapage operations
    PropositionDeRattrapage createProposition(PropositionDeRattrapage proposition);
    Optional<PropositionDeRattrapage> getPropositionById(Long id);
    List<PropositionDeRattrapage> getAllPropositions();
    PropositionDeRattrapage updateProposition(PropositionDeRattrapage proposition) throws CustomException;
    void deleteProposition(Long id) throws CustomException;

    // Signal operations
    Signal createSignal(Signal signal);
    Optional<Signal> getSignalById(Long id);
    List<Signal> getAllSignals();
    Signal updateSignal(Signal signal) throws CustomException;
    void deleteSignal(Long id) throws CustomException;

    // New Methods (for admin review actions):
    // Mark a teacher's signal as resolved (with an optional resolution message)
    void resolveFeedback(Long signalId, String resolution) throws CustomException;

    // Approve or reject a catch-up proposal
    void approveCatchUpProposal(Long proposalId) throws CustomException;
    void rejectCatchUpProposal(Long proposalId, String rejectionReason) throws CustomException;
}
