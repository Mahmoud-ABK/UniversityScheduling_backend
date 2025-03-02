package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.FrequenceType;
import com.scheduling.universityschedule_backend.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {

    @Query("""
    SELECT 
       s1, 
       s2,
       CONCAT(
          CASE WHEN s1.salle.id = s2.salle.id THEN 'Room Conflict; ' ELSE '' END,
          CASE WHEN s1.enseignant.id = s2.enseignant.id THEN 'Teacher Conflict; ' ELSE '' END,
          CASE WHEN EXISTS (
             SELECT 1 FROM Tp tp
             WHERE (
                   (tp MEMBER OF s1.tps)
                   OR EXISTS (SELECT 1 FROM s1.tds t1 WHERE t1 = tp.td)
                   OR EXISTS (
                         SELECT 1 FROM s1.branches b1 
                         WHERE b1 = tp.td.branch 
                           AND NOT EXISTS (SELECT 1 FROM s1.tds t2 WHERE t2.branch = b1)
                   )
             )
             AND (
                   (tp MEMBER OF s2.tps)
                   OR EXISTS (SELECT 1 FROM s2.tds t3 WHERE t3 = tp.td)
                   OR EXISTS (
                         SELECT 1 FROM s2.branches b2 
                         WHERE b2 = tp.td.branch 
                           AND NOT EXISTS (SELECT 1 FROM s2.tds t4 WHERE t4.branch = b2)
                   )
             )
          ) THEN 'Student Group Conflict; ' ELSE '' END,
          CASE WHEN s1.frequence = :biweekly AND s2.frequence = :biweekly THEN 'Bi-Weekly Conflict; ' ELSE '' END,
          CASE WHEN ((s1.frequence = :catchup AND s2.frequence = :biweekly)
                     OR (s1.frequence = :biweekly AND s2.frequence = :catchup))
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
              OR EXISTS (
                   SELECT 1 FROM Tp tp
                   WHERE (
                         (tp MEMBER OF s1.tps)
                         OR EXISTS (SELECT 1 FROM s1.tds t1 WHERE t1 = tp.td)
                         OR EXISTS (
                               SELECT 1 FROM s1.branches b1 
                               WHERE b1 = tp.td.branch 
                                 AND NOT EXISTS (SELECT 1 FROM s1.tds t2 WHERE t2.branch = b1)
                         )
                   )
                   AND (
                         (tp MEMBER OF s2.tps)
                         OR EXISTS (SELECT 1 FROM s2.tds t3 WHERE t3 = tp.td)
                         OR EXISTS (
                               SELECT 1 FROM s2.branches b2 
                               WHERE b2 = tp.td.branch 
                                 AND NOT EXISTS (SELECT 1 FROM s2.tds t4 WHERE t4.branch = b2)
                         )
                   )
              )
         )
    """)
    List<Object[]> findConflictingSeancePairs(@Param("biweekly") FrequenceType biweekly,
                                              @Param("catchup") FrequenceType catchup);

    @Query("""
    SELECT 
       s1, 
       s2,
       'Room Conflict' AS conflictTypes
    FROM Seance s1
    JOIN Seance s2 ON s1.jour = s2.jour AND s1.id < s2.id
    WHERE 
       s1.salle.id = s2.salle.id
       AND s1.heureDebut < s2.heureFin
       AND s1.heureFin > s2.heureDebut
       AND NOT (
           s1.frequence = :biweekly AND s2.frequence = :biweekly
       )
       AND NOT (
           s1.frequence = :catchup AND s2.frequence = :catchup AND s1.date != s2.date
       )
       AND NOT (
           (s1.frequence = :catchup AND s2.frequence = :biweekly)
           OR (s1.frequence = :biweekly AND s2.frequence = :catchup)
       )
    """)
    List<Object[]> findConflictingByRooms(@Param("biweekly") FrequenceType biweekly,
                                          @Param("catchup") FrequenceType catchup);

    @Query("""
    SELECT 
       s,
       CONCAT(
          CASE WHEN s.salle.id = s_main.salle.id THEN 'Room Conflict; ' ELSE '' END,
          CASE WHEN s.enseignant.id = s_main.enseignant.id THEN 'Teacher Conflict; ' ELSE '' END,
          CASE WHEN EXISTS (
             SELECT 1 FROM Tp tp
             WHERE (
                   (tp MEMBER OF s.tps)
                   OR EXISTS (SELECT 1 FROM s.tds t1 WHERE t1 = tp.td)
                   OR EXISTS (
                         SELECT 1 FROM s.branches b1 
                         WHERE b1 = tp.td.branch 
                           AND NOT EXISTS (SELECT 1 FROM s.tds t2 WHERE t2.branch = b1)
                   )
             )
             AND (
                   (tp MEMBER OF s_main.tps)
                   OR EXISTS (SELECT 1 FROM s_main.tds t3 WHERE t3 = tp.td)
                   OR EXISTS (
                         SELECT 1 FROM s_main.branches b2 
                         WHERE b2 = tp.td.branch 
                           AND NOT EXISTS (SELECT 1 FROM s_main.tds t4 WHERE t4.branch = b2)
                   )
             )
          ) THEN 'Student Group Conflict; ' ELSE '' END,
          CASE WHEN s.frequence = :biweekly AND s_main.frequence = :biweekly THEN 'Bi-Weekly Conflict; ' ELSE '' END,
          CASE WHEN ((s.frequence = :catchup AND s_main.frequence = :biweekly)
                     OR (s.frequence = :biweekly AND s_main.frequence = :catchup))
               THEN 'Catch-Up Exception; ' ELSE '' END
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
             OR EXISTS (
                   SELECT 1 FROM Tp tp
                   WHERE (
                         (tp MEMBER OF s.tps)
                         OR EXISTS (SELECT 1 FROM s.tds t1 WHERE t1 = tp.td)
                         OR EXISTS (
                               SELECT 1 FROM s.branches b1 
                               WHERE b1 = tp.td.branch 
                                 AND NOT EXISTS (SELECT 1 FROM s.tds t2 WHERE t2.branch = b1)
                         )
                   )
                   AND (
                         (tp MEMBER OF s_main.tps)
                         OR EXISTS (SELECT 1 FROM s_main.tds t3 WHERE t3 = tp.td)
                         OR EXISTS (
                               SELECT 1 FROM s_main.branches b2 
                               WHERE b2 = tp.td.branch 
                                 AND NOT EXISTS (SELECT 1 FROM s_main.tds t4 WHERE t4.branch = b2)
                         )
                   )
             )
         )
    """)
    List<Object[]> findRoomConflictsForSeance(@Param("seanceId") Long seanceId,
                                              @Param("biweekly") FrequenceType biweekly,
                                              @Param("catchup") FrequenceType catchup);
}
