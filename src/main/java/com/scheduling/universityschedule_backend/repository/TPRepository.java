package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.TP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TPRepository extends JpaRepository<TP, Long> {
}
