package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.model.TP;
import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    // Seance operations
    Seance createSeance(Seance seance);
    Optional<Seance> getSeanceById(Long id);
    List<Seance> getAllSeances();
    Seance updateSeance(Seance seance);
    void deleteSeance(Long id);

    // TD (Tutorial) operations
    TD createTD(TD td);
    Optional<TD> getTDById(Long id);
    List<TD> getAllTDs();
    TD updateTD(TD td);
    void deleteTD(Long id);

    // TP (Practical) operations
    TP createTP(TP tp);
    Optional<TP> getTPById(Long id);
    List<TP> getAllTPs();
    TP updateTP(TP tp);
    void deleteTP(Long id);
}
