package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
}
