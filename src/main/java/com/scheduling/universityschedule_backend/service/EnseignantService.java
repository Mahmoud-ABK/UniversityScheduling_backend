package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing teachers
 */
public interface EnseignantService {
    List<SeanceDTO> getEmploiDuTemps(Long id) throws CustomException;
    int getHeuresEnseignees(Long id) throws CustomException;
    PropositionDeRattrapageDTO soumettreDemandeRattrapage(Long id, PropositionDeRattrapageDTO propositionDTO) throws CustomException ;
    SignalDTO soumettreSuggestion(Long id, SignalDTO signalDTO) throws CustomException ;
    List<SignalDTO> getSignalisations(Long id) throws CustomException ;
}
