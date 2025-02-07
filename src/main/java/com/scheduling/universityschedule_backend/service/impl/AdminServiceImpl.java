package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Signal;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.repository.SignalRepository;
import com.scheduling.universityschedule_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final SignalRepository signalRepository;
    private final PropositionDeRattrapageRepository propositionRepository;
    private final FichierExcelRepository fichierExcelRepository;

    @Autowired
    public AdminServiceImpl(SignalRepository signalRepository,
                            PropositionDeRattrapageRepository propositionRepository,
                            FichierExcelRepository fichierExcelRepository) {
        this.signalRepository = signalRepository;
        this.propositionRepository = propositionRepository;
        this.fichierExcelRepository = fichierExcelRepository;
    }

    // Fetch all teacher signals for review
    @Override
    public List<Signal> fetchAllSignals() {
        return signalRepository.findAll();
    }

    // Mark a signal as resolved
    @Override
    public void markSignalResolved(Long signalId, String resolution) throws CustomException {
        Optional<Signal> optionalSignal = signalRepository.findById(signalId);
        if (optionalSignal.isEmpty()) {
            throw new CustomException("Signal not found with id: " + signalId);
        }
        Signal signal = optionalSignal.get();
        signalRepository.save(signal);
    }

    // Fetch all catch-up proposals submitted by teachers
    @Override
    public List<PropositionDeRattrapage> fetchAllCatchUpProposals() {
        return propositionRepository.findAll();
    }

    // Approve a catch-up proposal
    @Override
    public void approveCatchUpProposal(Long proposalId) throws CustomException {
        Optional<PropositionDeRattrapage> optionalProp = propositionRepository.findById(proposalId);
        if (optionalProp.isEmpty()) {
            throw new CustomException("Proposition not found with id: " + proposalId);
        }
        PropositionDeRattrapage proposition = optionalProp.get();
        proposition.setStatus("approved");
        // Optionally clear any rejection reason
        proposition.setReason(null);
        propositionRepository.save(proposition);
    }

    // Reject a catch-up proposal
    @Override
    public void rejectCatchUpProposal(Long proposalId, String rejectionReason) throws CustomException {
        Optional<PropositionDeRattrapage> optionalProp = propositionRepository.findById(proposalId);
        if (optionalProp.isEmpty()) {
            throw new CustomException("Proposition not found with id: " + proposalId);
        }
        PropositionDeRattrapage proposition = optionalProp.get();
        proposition.setStatus("rejected");
        proposition.setReason(rejectionReason);
        propositionRepository.save(proposition);
    }

    // Import Excel data from a MultipartFile
    @Override
    public FichierExcel importExcelData(MultipartFile file) throws CustomException {
        if (file == null || file.isEmpty()) {
            throw new CustomException("File is empty or null");
        }
        FichierExcel fichierExcel = new FichierExcel();
        fichierExcel.setFileName(file.getOriginalFilename());
        fichierExcel.setStatus("imported");
        // Initialize errors as an empty list
        fichierExcel.setErrors(new ArrayList<>());
        fichierExcel.setImportDate(LocalDateTime.now());
        return fichierExcelRepository.save(fichierExcel);
    }
}
