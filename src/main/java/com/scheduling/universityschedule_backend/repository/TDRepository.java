package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.TD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TDRepository extends JpaRepository<TD, Long> {
}
