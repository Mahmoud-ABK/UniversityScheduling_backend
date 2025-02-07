package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Signal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {
    // Fetch all teacher signals for review
    List<Signal> fetchAllSignals();

    // Mark a signal as resolved
    void markSignalResolved(Long signalId, String resolution) throws CustomException;

    // Fetch all catch-up proposals submitted by teachers
    List<PropositionDeRattrapage> fetchAllCatchUpProposals();

    // Approve or reject catch-up proposals
    void approveCatchUpProposal(Long proposalId) throws CustomException;
    void rejectCatchUpProposal(Long proposalId, String rejectionReason) throws CustomException;

    // Import Excel data (if you wish to have this separately from ResourceService)
    FichierExcel importExcelData(MultipartFile file) throws CustomException;
}
