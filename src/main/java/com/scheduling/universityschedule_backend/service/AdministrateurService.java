package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing Administrator operations
 */
public interface AdministrateurService {
    FichierExcelDTO importEmploiDuTemps(FichierExcelDTO fichierExcelDTO) throws CustomException;
    List<SeanceDTO> genererEmploiDuTemps() throws CustomException ;
    PropositionDeRattrapageDTO traiterDemandeRattrapage(Long id, boolean approved) throws CustomException ;
    List<NotificationDTO> diffuserNotification(NotificationDTO notificationDTO) throws CustomException ;
}
