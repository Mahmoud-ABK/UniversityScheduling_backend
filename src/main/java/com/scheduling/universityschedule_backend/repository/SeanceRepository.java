package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
}
