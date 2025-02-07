package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.model.TP;
import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    // Seance operations
    Seance createSeance(Seance seance);
    Seance getSeanceById(Long id) throws CustomException;
    List<Seance> getAllSeances();
    Seance updateSeance(Seance seance) throws CustomException;

    void updateSeance(Long seanceId, Seance seance) throws CustomException;

    void deleteSeance(Long id) throws CustomException;

    // TD (Tutorial) operations
    TD createTD(TD td);
    Optional<TD> getTDById(Long id);
    List<TD> getAllTDs();
    TD updateTD(TD td) throws CustomException;

    void updateTD(Long tdId, TD td) throws CustomException;

    void deleteTD(Long id) throws CustomException;

    // TP (Practical) operations
    TP createTP(TP tp);
    Optional<TP> getTPById(Long id);
    List<TP> getAllTPs();
    TP updateTP(TP tp) throws CustomException;

    void updateTP(Long tpId, TP tp) throws CustomException;

    void deleteTP(Long id) throws CustomException;

    // Return sessions for a given teacher (for his/her schedule view)
    List<Seance> getSeancesForTeacher(Long enseignantId);

    // Return sessions for a given student (by branch or direct enrollment)
    List<Seance> getSeancesForStudent(Long etudiantId);

    // Retrieve the total teaching hours for a teacher
    int getTeachingHoursForTeacher(Long enseignantId) throws CustomException;

    // Optionally, query sessions by branch/program (for a student’s perspective)
    List<Seance> getSeancesByBranche(Long brancheId) throws CustomException;
}
