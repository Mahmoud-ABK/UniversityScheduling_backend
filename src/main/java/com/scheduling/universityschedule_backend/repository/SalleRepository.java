package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long> {
}
