package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
}
