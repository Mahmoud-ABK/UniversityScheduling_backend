package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    @Query("""
        SELECT s1, s2
        FROM Seance s1
        JOIN Seance s2 ON s1.jour = s2.jour AND s1.id < s2.id
        WHERE s1.heureDebut < s2.heureFin AND s1.heureFin > s2.heureDebut
          AND (
                s1.salle.id = s2.salle.id
                OR s1.enseignant.id = s2.enseignant.id
                OR EXISTS (SELECT 1 FROM s1.branches b1 JOIN s2.branches b2 WHERE b1.id = b2.id)
                OR EXISTS (SELECT 1 FROM s1.tds td1 JOIN s2.tds td2 WHERE td1.id = td2.id)
                OR EXISTS (SELECT 1 FROM s1.tps tp1 JOIN s2.tps tp2 WHERE tp1.id = tp2.id)
              )
          AND (NOT (
                  (
                    (s1.frequence <> '' AND s1.frequence <> '1/15' AND s2.frequence = '1/15')
                    OR (s1.frequence = '1/15' AND s2.frequence <> '' AND s2.frequence <> '1/15')
                  )
                  AND s1.salle.id = s2.salle.id
                  AND (
                       EXISTS (SELECT 1 FROM s1.branches b1 JOIN s2.branches b2 WHERE b1.id = b2.id)
                       OR EXISTS (SELECT 1 FROM s1.tds td1 JOIN s2.tds td2 WHERE td1.id = td2.id)
                       OR EXISTS (SELECT 1 FROM s1.tps tp1 JOIN s2.tps tp2 WHERE tp1.id = tp2.id)
                  )
                 ))
        """)
    List<Object[]> findConflictingSeancePairs();
    @Query("""
            SELECT s1, s2
            FROM Seance s1
            JOIN Seance s2 ON s1.jour = s2.jour AND s1.id < s2.id
            WHERE 
                s1.salle.id = s2.salle.id
                AND s1.heureDebut < s2.heureFin
                AND s1.heureFin > s2.heureDebut
           """)
    List<Object[]> findConflictingByRooms();
    @Query("""
        SELECT s
        FROM Seance s
        WHERE 
            s.id <> :seanceId
            AND s.jour = :jour
            AND s.salle.id = :salleId
            AND s.heureDebut < :heureFin
            AND s.heureFin > :heureDebut
       """)
    List<Seance> findRoomConflictsForSeance(@Param("seanceId") Long seanceId,
                                            @Param("jour") String jour,
                                            @Param("salleId") Long salleId,
                                            @Param("heureDebut") String heureDebut,
                                            @Param("heureFin") String heureFin);

}
