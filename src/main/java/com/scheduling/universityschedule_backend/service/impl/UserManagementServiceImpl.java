package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.Personne;
import com.scheduling.universityschedule_backend.repository.PersonneRepository;
import com.scheduling.universityschedule_backend.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final PersonneRepository personneRepository;

    @Autowired
    public UserManagementServiceImpl(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    @Override
    public Personne createUser(Personne personne) {
        return personneRepository.save(personne);
    }

    @Override
    public Personne updateUser(Long userId, Personne updatedPersonne) throws CustomException {
        Optional<Personne> optionalPersonne = personneRepository.findById(userId);
        if (optionalPersonne.isEmpty()) {
            throw new CustomException("User not found with ID: " + userId);
        }

        Personne existingPersonne = optionalPersonne.get();
        existingPersonne.setNom(updatedPersonne.getNom());
        existingPersonne.setPrenom(updatedPersonne.getPrenom());
        existingPersonne.setEmail(updatedPersonne.getEmail());
     //   existingPersonne.setRole(updatedPersonne.getRole());

        return personneRepository.save(existingPersonne);
    }

    @Override
    public void deleteUser(Long userId) throws CustomException {
        if (!personneRepository.existsById(userId)) {
            throw new CustomException("User not found with ID: " + userId);
        }
        personneRepository.deleteById(userId);
    }

    @Override
    public Personne getUserById(Long userId) throws CustomException {
        return personneRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with ID: " + userId));
    }

    @Override
    public List<Personne> getAllUsers() {
        return personneRepository.findAll();
    }

    @Override
    public void assignRole(Long userId, String role) throws CustomException {
        Optional<Personne> optionalPersonne = personneRepository.findById(userId);
        if (optionalPersonne.isEmpty()) {
            throw new CustomException("User not found with ID: " + userId);
        }

        Personne personne = optionalPersonne.get();
       
        personneRepository.save(personne);
    }
}
