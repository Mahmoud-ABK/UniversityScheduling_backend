package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {
    // Fetch all teacher signals for review
    List<SignalDTO> fetchAllSignals();

    // Mark a signal as resolved
    void markSignalResolved(Long signalId, String resolution) throws CustomException;

    // Fetch all catch-up proposals submitted by teachers
    List<PropositionDeRattrapageDTO> fetchAllCatchUpProposals();

    // Approve or reject catch-up proposals
    void approveCatchUpProposal(Long proposalId) throws CustomException;
    void rejectCatchUpProposal(Long proposalId, String rejectionReason) throws CustomException;

    // Import Excel data
    FichierExcelDTO importExcelData(MultipartFile file) throws CustomException;

    // Retrieve only the unread notifications for a user
    List<NotificationDTO> getUnreadNotifications(Long userId) throws CustomException;

    // Retrieve a list of available rooms for a given time slot
    List<SalleDTO> getAvailableSalles(String timeSlot) throws CustomException;
}
