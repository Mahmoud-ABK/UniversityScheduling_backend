package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.FichierExcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichierExcelRepository extends JpaRepository<FichierExcel, Long> {
}
