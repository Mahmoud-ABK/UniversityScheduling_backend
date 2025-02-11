package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for make-up session proposals
 */
public interface PropositionDeRattrapageService {
    PropositionDeRattrapageDTO submitProposal(PropositionDeRattrapageDTO propositionDTO) throws CustomException;
    List<PropositionDeRattrapageDTO> getAllProposals() throws CustomException;
    PropositionDeRattrapageDTO approveOrRejectProposal(Long id, boolean approved) throws CustomException;
}
