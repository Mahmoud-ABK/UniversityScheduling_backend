package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing students
 */
public interface EtudiantService {
    List<SeanceDTO> getEmploiDuTempsPersonnel(Long id) throws CustomException;
    List<SeanceDTO> getEmploiDuTempsBranche(Long brancheId) throws CustomException ;
    List<NotificationDTO> getNotifications(Long id) throws CustomException ;
}
