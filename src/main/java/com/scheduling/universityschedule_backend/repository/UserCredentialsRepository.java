package com.scheduling.universityschedule_backend.repository;

import com.scheduling.universityschedule_backend.model.UserCredentials;
import com.scheduling.universityschedule_backend.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {

    Optional<UserCredentials> findByUsername(String username);

    Optional<UserCredentials> findByPersonne(Personne personne);

    boolean existsByUsername(String username);

    // Additional useful methods
    Optional<UserCredentials> findByPersonneId(Long personneId);

    void deleteByUsername(String username);
}