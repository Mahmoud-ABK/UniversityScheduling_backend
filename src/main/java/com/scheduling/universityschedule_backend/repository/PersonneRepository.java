package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
}
