package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Etudiant;
import com.scheduling.universityschedule_backend.model.TD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TDRepository extends JpaRepository<TD, Long> {
    @Query("SELECT e FROM Etudiant e WHERE e.branche.id = :brancheId")
    List<Etudiant> findAllEtudiantsByBrancheId(@Param("brancheId") Long brancheId);

    @Query("SELECT e FROM Etudiant e WHERE e.tp.td.id = :tdId")
    List<Etudiant> findAllEtudiantsByTdId(@Param("tdId") Long tdId);
}
