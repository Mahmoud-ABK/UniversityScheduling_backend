package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.service.PropositionDeRattrapageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PropositionDeRattrapageServiceImpl implements PropositionDeRattrapageService {

    private final PropositionDeRattrapageRepository propositionRepo;
    private final EntityMapper mapper;

    @Autowired
    public PropositionDeRattrapageServiceImpl(PropositionDeRattrapageRepository propositionRepo, EntityMapper mapper) {
        this.propositionRepo = propositionRepo;
        this.mapper = mapper;
    }

    /**
     * Submits a catch-up session proposal.
     */
    @Override
    public PropositionDeRattrapageDTO submitProposal(PropositionDeRattrapageDTO propositionDTO) throws CustomException {
        try {
            PropositionDeRattrapage proposal = mapper.toPropositionDeRattrapage(propositionDTO);
            proposal.setStatus("pending");
            proposal = propositionRepo.save(proposal);
            return mapper.toPropositionDeRattrapageDTO(proposal);
        } catch (Exception e) {
            throw new CustomException("Failed to submit proposal", e);
        }
    }

    /**
     * Retrieves all proposals.
     */
    @Override
    public List<PropositionDeRattrapageDTO> getAllProposals() throws CustomException {
        List<PropositionDeRattrapageDTO> dtoList = new ArrayList<>();
        propositionRepo.findAll().forEach(prop -> dtoList.add(mapper.toPropositionDeRattrapageDTO(prop)));
        return dtoList;
    }

    /**
     * Approves or rejects a proposal.
     */
    @Override
    public PropositionDeRattrapageDTO approveOrRejectProposal(Long id, boolean approved) throws CustomException {
        PropositionDeRattrapage proposal = propositionRepo.findById(id)
                .orElseThrow(() -> new CustomException("Proposal not found"));
        proposal.setStatus(approved ? "approved" : "rejected");
        proposal = propositionRepo.save(proposal);
        return mapper.toPropositionDeRattrapageDTO(proposal);
    }
}
