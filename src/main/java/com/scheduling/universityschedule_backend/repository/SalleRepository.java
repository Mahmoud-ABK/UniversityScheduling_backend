package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
    /**
     * Finds all rooms that are NOT occupied during the specified time slot.
     *
     * This query uses a NOT EXISTS subquery to exclude rooms that have sessions that
     * overlap with the requested time frame. The query accounts for:
     * 1. Day of week matching
     * 2. Time overlap checking
     * 3. Special handling for bi-weekly and catch-up sessions
     *
     * @param jour Day of week for the requested time slot
     * @param heureDebut Start time for the requested slot
     * @param heureFin End time for the requested slot
     * @param date Specific date for catch-up sessions (can be null)
     * @return List of available rooms during the specified time
     */
    @Query("""
        SELECT s FROM Salle s
        WHERE NOT EXISTS (
            SELECT 1 FROM Seance seance
            WHERE seance.salle = s
              AND seance.jour = :jour
              AND seance.heureDebut < :heureFin
              AND seance.heureFin > :heureDebut
              AND (
                  seance.frequence = 'WEEKLY' 
                  OR (
                      seance.frequence = 'CATCHUP' 
                      AND (:date IS NULL OR seance.date = :date)
                  )
              )
        )
        ORDER BY s.identifiant
    """)
    List<Salle> findAvailableRooms(
            @Param("jour") DayOfWeek jour,
            @Param("heureDebut") LocalTime heureDebut,
            @Param("heureFin") LocalTime heureFin,
            @Param("date") LocalDate date
    );
}
