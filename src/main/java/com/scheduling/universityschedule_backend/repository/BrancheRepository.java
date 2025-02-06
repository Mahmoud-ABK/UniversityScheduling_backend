package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Branche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrancheRepository extends JpaRepository<Branche, Long> {
}
