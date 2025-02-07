package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    // PropositionDeRattrapage operations
    PropositionDeRattrapageDTO createProposition(PropositionDeRattrapageDTO proposition);
    Optional<PropositionDeRattrapageDTO> getPropositionById(Long id);
    List<PropositionDeRattrapageDTO> getAllPropositions();
    PropositionDeRattrapageDTO updateProposition(PropositionDeRattrapageDTO proposition) throws CustomException;
    void deleteProposition(Long id) throws CustomException;

    // Signal operations
    SignalDTO createSignal(SignalDTO signal);
    Optional<SignalDTO> getSignalById(Long id);
    List<SignalDTO> getAllSignals();
    SignalDTO updateSignal(SignalDTO signal) throws CustomException;
    void deleteSignal(Long id) throws CustomException;

    // Mark a teacher's signal as resolved (with an optional resolution message)
    void resolveFeedback(Long signalId, String resolution) throws CustomException;

    // Approve or reject a catch-up proposal
    void approveCatchUpProposal(Long proposalId) throws CustomException;
    void rejectCatchUpProposal(Long proposalId, String rejectionReason) throws CustomException;
}
