package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    @Query(value = """
    SELECT 
       s1, 
       s2,
       CONCAT(
          CASE WHEN s1.salle.id = s2.salle.id THEN 'Room Conflict; ' ELSE '' END,
          CASE WHEN s1.enseignant.id = s2.enseignant.id THEN 'Teacher Conflict; ' ELSE '' END,
          CASE WHEN 
             (EXISTS (SELECT 1 FROM s1.branches b1 JOIN s2.branches b2 WHERE b1.id = b2.id)
              AND EXISTS (SELECT 1 FROM s1.tds td1 JOIN s2.tds td2 WHERE td1.id = td2.id)
              AND EXISTS (SELECT 1 FROM s1.tps tp1 JOIN s2.tps tp2 WHERE tp1.id = tp2.id))
             THEN 'Student Group Conflict; ' ELSE '' END,
          CASE WHEN s1.frequence = '1/15' AND s2.frequence = '1/15' THEN 'Bi-Weekly Conflict; ' ELSE '' END,
          CASE WHEN ((s1.frequence = 'catch-up' AND s2.frequence = '1/15')
                     OR (s1.frequence = '1/15' AND s2.frequence = 'catch-up'))
                   THEN 'Catch-Up Exception; ' ELSE '' END
       ) AS conflictTypes 
    FROM Seance s1
    JOIN Seance s2 ON s1.jour = s2.jour AND s1.id < s2.id
    WHERE 
         s1.heureDebut < s2.heureFin 
         AND s1.heureFin > s2.heureDebut
         AND (
              s1.salle.id = s2.salle.id
              OR s1.enseignant.id = s2.enseignant.id
              OR (EXISTS (SELECT 1 FROM s1.branches b1 JOIN s2.branches b2 WHERE b1.id = b2.id)
                  AND EXISTS (SELECT 1 FROM s1.tds td1 JOIN s2.tds td2 WHERE td1.id = td2.id)
                  AND EXISTS (SELECT 1 FROM s1.tps tp1 JOIN s2.tps tp2 WHERE tp1.id = tp2.id)
                 )
         )
         AND NOT (
              (
                (s1.frequence = '1/15' AND s2.frequence = '1/15')
                OR ( (s1.frequence = 'catch-up' AND s2.frequence = '1/15')
                     OR (s1.frequence = '1/15' AND s2.frequence = 'catch-up')
                   )
              )
              AND EXISTS (SELECT 1 FROM s1.branches b1 JOIN s2.branches b2 WHERE b1.id = b2.id)
              AND EXISTS (SELECT 1 FROM s1.tds td1 JOIN s2.tds td2 WHERE td1.id = td2.id)
              AND EXISTS (SELECT 1 FROM s1.tps tp1 JOIN s2.tps tp2 WHERE tp1.id = tp2.id)
         )
""")
    List<Object[]> findConflictingSeancePairs();


    @Query("""
    SELECT 
       s1, 
       s2,
       "Room Conflict" AS conflictTypes
    FROM Seance s1
    JOIN Seance s2 ON s1.jour = s2.jour AND s1.id < s2.id
    WHERE 
         s1.salle.id = s2.salle.id
         AND s1.heureDebut < s2.heureFin
         AND s1.heureFin > s2.heureDebut
""")
    List<Object[]> findConflictingByRooms();

    @Query("""
    SELECT 
       s,
       CONCAT(
          CASE WHEN s.salle.id = s_main.salle.id THEN 'Room Conflict; ' ELSE '' END,
          CASE WHEN s.enseignant.id = s_main.enseignant.id THEN 'Teacher Conflict; ' ELSE '' END,
          CASE WHEN 
             EXISTS (SELECT 1 FROM s.branches b WHERE b IN (SELECT b2 FROM s_main.branches b2))
             AND EXISTS (SELECT 1 FROM s.tds td WHERE td IN (SELECT td2 FROM s_main.tds td2))
             AND EXISTS (SELECT 1 FROM s.tps tp WHERE tp IN (SELECT tp2 FROM s_main.tps tp2))
          THEN 'Student Group Conflict; ' ELSE '' END
       ) AS conflictTypes
    FROM Seance s
    JOIN Seance s_main ON s_main.id = :seanceId
    WHERE 
         s.id <> s_main.id
         AND s.jour = s_main.jour
         AND s.heureDebut < s_main.heureFin
         AND s.heureFin > s_main.heureDebut
         AND (
             s.salle.id = s_main.salle.id
             OR s.enseignant.id = s_main.enseignant.id
             OR (
                EXISTS (SELECT 1 FROM s.branches b WHERE b IN (SELECT b2 FROM s_main.branches b2))
                AND EXISTS (SELECT 1 FROM s.tds td WHERE td IN (SELECT td2 FROM s_main.tds td2))
                AND EXISTS (SELECT 1 FROM s.tps tp WHERE tp IN (SELECT tp2 FROM s_main.tps tp2))
             )
         )
        """)
    List<Object[]> findRoomConflictsForSeance(@Param("seanceId") Long seanceId);


}
