package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.TDDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    // Seance operations
    SeanceDTO createSeance(SeanceDTO seance);
    SeanceDTO getSeanceById(Long id) throws CustomException;
    List<SeanceDTO> getAllSeances();
    SeanceDTO updateSeance(SeanceDTO seance) throws CustomException;
    void deleteSeance(Long id) throws CustomException;

    // TD (Tutorial) operations
    TDDTO createTD(TDDTO td);
    Optional<TDDTO> getTDById(Long id);
    List<TDDTO> getAllTDs();
    TDDTO updateTD(TDDTO td) throws CustomException;
    void deleteTD(Long id) throws CustomException;

    // TP (Practical) operations
    TPDTO createTP(TPDTO tp);
    Optional<TPDTO> getTPById(Long id);
    List<TPDTO> getAllTPs();
    TPDTO updateTP(TPDTO tp) throws CustomException;
    void deleteTP(Long id) throws CustomException;

    // Return sessions for a given teacher (for his/her schedule view)
    List<SeanceDTO> getSeancesForTeacher(Long enseignantId);

    // Return sessions for a given student (by branch or direct enrollment)
    List<SeanceDTO> getSeancesForStudent(Long etudiantId);

    // Retrieve the total teaching hours for a teacher
    int getTeachingHoursForTeacher(Long enseignantId) throws CustomException;

    // Query sessions by branch/program (for a student’s perspective)
    List<SeanceDTO> getSeancesByBranche(Long brancheId) throws CustomException;
    // Query sessions by TP and TD
    List<SeanceDTO> getSeancesByTP(Long TPId) throws CustomException;
    List<SeanceDTO> getSeancesByTD(Long TDId) throws CustomException;

}
