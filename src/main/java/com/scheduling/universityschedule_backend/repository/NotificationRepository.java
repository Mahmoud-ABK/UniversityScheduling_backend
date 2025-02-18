package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByIsreadFalse();
    /**
     * Custom query to get all notifications for a specific Personne ID (destination).
     * @param personneId the ID of the Personne (destination)
     * @return List of notifications for the specified Personne ID
     */
    @Query("SELECT n FROM Notification n WHERE n.recepteur.id = :personneId")
    List<Notification> findAllByPersonneId(@Param("personneId") Long personneId);
}
