package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalRepository extends JpaRepository<Signal, Long> {
}
