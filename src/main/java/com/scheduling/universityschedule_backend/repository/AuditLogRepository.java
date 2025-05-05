package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserLogin(String userLogin);

    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Additional useful methods
    @Query("SELECT a FROM AuditLog a WHERE a.userLogin = :userLogin AND a.timestamp BETWEEN :start AND :end")
    List<AuditLog> findByUserLoginAndTimeRange(
            @Param("userLogin") String userLogin,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<AuditLog> findByUserLoginAndTimestampAfter(String userLogin, LocalDateTime after);

    List<AuditLog> findTop10ByUserLoginOrderByTimestampDesc(String userLogin);

    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId")
    List<AuditLog> findByEntityTypeAndEntityId(
            @Param("entityType") String entityType,
            @Param("entityId") Long entityId
    );
}