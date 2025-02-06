package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropositionDeRattrapageRepository extends JpaRepository<PropositionDeRattrapage, Long> {
}
