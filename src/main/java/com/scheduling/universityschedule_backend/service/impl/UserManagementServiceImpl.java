package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.model.enums.AuditAction;
import com.scheduling.universityschedule_backend.model.enums.UserRole;
import com.scheduling.universityschedule_backend.model.enums.UserStatus;
import com.scheduling.universityschedule_backend.repository.AuditLogRepository;
import com.scheduling.universityschedule_backend.repository.UserCredentialsRepository;
import com.scheduling.universityschedule_backend.service.*;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdministrateurService administrateurService;
    private final TechnicienService technicienService;
    private final EnseignantService enseignantService;
    private final EtudiantService etudiantService;
    private final EntityMapper entityMapper;

    public UserManagementServiceImpl(
            UserCredentialsRepository userCredentialsRepository,
            AuditLogRepository auditLogRepository,
            PasswordEncoder passwordEncoder,
            AdministrateurService administrateurService,
            TechnicienService technicienService,
            EnseignantService enseignantService,
            EtudiantService etudiantService,
            EntityMapper entityMapper) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.administrateurService = administrateurService;
        this.technicienService = technicienService;
        this.enseignantService = enseignantService;
        this.etudiantService = etudiantService;
        this.entityMapper = entityMapper;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TECHNICIAN')")
    public UserDTO findById(Long id) throws CustomException {
        UserCredentials credentials = userCredentialsRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));
        return toUserDTO(credentials);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TECHNICIAN')")
    public List<UserDTO> findAll() throws CustomException {

        ArrayList<UserDTO> returnedlist = new ArrayList<>();
        userCredentialsRepository.findAll().stream().forEach(
                userCredentials -> {
                    try {
                        returnedlist.add(this.toUserDTO(userCredentials));
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        if (returnedlist.isEmpty()) {
            throw new CustomException("No users found");
        }
        return returnedlist;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TECHNICIAN')")
    @Transactional
    public UserDTO create(UserDTO userDTO) throws CustomException {
        if (userCredentialsRepository.existsByUsername(userDTO.getUsername())) {
            throw new CustomException("Username already exists: " + userDTO.getUsername());
        }

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername(userDTO.getUsername());
        credentials.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        credentials.setRole(userDTO.getRole());
        credentials.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : UserStatus.ACTIVE);

        // Create Personne entity based on role
        Personne personne = createPersonne(userDTO.getPersonneData(), userDTO.getRole());
        credentials.setPersonne(personne);

        userCredentialsRepository.save(credentials);

        auditLogRepository.save(AuditLog.builder()
                .userLogin(credentials.getUsername())
                .action(AuditAction.USER_CREATED.name())
                .entityType("UserCredentials")
                .entityId(credentials.getId())
                .details("Performed by technician: " + getCurrentUsername())
                .timestamp(LocalDateTime.now())
                .build());

        return toUserDTO(credentials);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TECHNICIAN')")
    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) throws CustomException {
        UserCredentials credentials = userCredentialsRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));

        if (!credentials.getUsername().equals(userDTO.getUsername()) &&
                userCredentialsRepository.existsByUsername(userDTO.getUsername())) {
            throw new CustomException("Username already exists: " + userDTO.getUsername());
        }

        credentials.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            credentials.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            credentials.setLastPasswordChange(LocalDateTime.now());
        }
        credentials.setRole(userDTO.getRole());
        credentials.setStatus(userDTO.getStatus());

        // Update Personne entity based on role
        updatePersonne(credentials.getPersonne(), userDTO.getPersonneData(), userDTO.getRole());

        userCredentialsRepository.save(credentials);

        auditLogRepository.save(AuditLog.builder()
                .userLogin(credentials.getUsername())
                .action(AuditAction.USER_UPDATED.name())
                .entityType("UserCredentials")
                .entityId(credentials.getId())
                .details("Performed by technician: " + getCurrentUsername())
                .timestamp(LocalDateTime.now())
                .build());

        return toUserDTO(credentials);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TECHNICIAN')")
    @Transactional
    public void delete(Long id) throws CustomException {
        UserCredentials credentials = userCredentialsRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));

        // Delete Personne entity based on role
        deletePersonne(credentials.getPersonne(), credentials.getRole());

        userCredentialsRepository.delete(credentials);

        auditLogRepository.save(AuditLog.builder()
                .userLogin(credentials.getUsername())
                .action(AuditAction.USER_DELETED.name())
                .entityType("UserCredentials")
                .entityId(id)
                .details("Performed by technician: " + getCurrentUsername())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_TECHNICIAN')")
    public UserDTO findByPersonneId(Long personneId) throws CustomException {
        UserCredentials credentials = userCredentialsRepository.findByPersonneId(personneId)
                .orElseThrow(() -> new CustomException("User not found with Personne ID: " + personneId));
        return toUserDTO(credentials);
    }

    private UserDTO toUserDTO(UserCredentials credentials) throws CustomException {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(credentials.getId());
        userDTO.setPersonneId(credentials.getPersonne().getId());
        userDTO.setUsername(credentials.getUsername());
        userDTO.setRole(credentials.getRole());
        userDTO.setStatus(credentials.getStatus());

        // Map Personne to appropriate DTO based on role
        Personne personne = credentials.getPersonne();
        switch (credentials.getRole()) {
            case ROLE_ADMIN:
                userDTO.setPersonneData(entityMapper.toAdministrateurDTO((Administrateur) personne));
                break;
            case ROLE_TECHNICIAN:
                userDTO.setPersonneData(entityMapper.toTechnicienDTO((Technicien) personne));
                break;
            case ROLE_TEACHER:
                userDTO.setPersonneData(entityMapper.toEnseignantDTO((Enseignant) personne));
                break;
            case ROLE_STUDENT:
                userDTO.setPersonneData(entityMapper.toEtudiantDTO((Etudiant) personne));
                break;
            default:
                throw new CustomException("Unsupported role: " + credentials.getRole());
        }

        return userDTO;
    }

    private Personne createPersonne(Object personneData, UserRole role) throws CustomException {
        switch (role) {
            case ROLE_ADMIN:
                AdministrateurDTO adminDTO = (AdministrateurDTO) personneData;
                Administrateur admin = entityMapper.toAdministrateur(adminDTO);
                administrateurService.create(adminDTO); // Assuming save persists via service
                return admin;
            case ROLE_TECHNICIAN:
                TechnicienDTO techDTO = (TechnicienDTO) personneData;
                Technicien technicien = entityMapper.toTechnicien(techDTO);
                technicienService.create(techDTO); // Assuming save persists via service
                return technicien;
            case ROLE_TEACHER:
                EnseignantDTO teacherDTO = (EnseignantDTO) personneData;
                Enseignant enseignant = entityMapper.toEnseignant(teacherDTO);
                enseignantService.create(teacherDTO); // Assuming save persists via service
                return enseignant;
            case ROLE_STUDENT:
                EtudiantDTO studentDTO = (EtudiantDTO) personneData;
                Etudiant etudiant = entityMapper.toEtudiant(studentDTO);
                etudiantService.create(studentDTO); // Assuming save persists via service
                return etudiant;
            default:
                throw new CustomException("Unsupported role: " + role);
        }
    }

    private void updatePersonne(Personne personne, Object personneData, UserRole role) throws CustomException {
        switch (role) {
            case ROLE_ADMIN:
                AdministrateurDTO adminDTO = (AdministrateurDTO) personneData;
                entityMapper.updateFromDto(adminDTO, (Administrateur) personne);
                administrateurService.update(personne.getId(), adminDTO);
                break;
            case ROLE_TECHNICIAN:
                TechnicienDTO techDTO = (TechnicienDTO) personneData;
                entityMapper.updateFromDto(techDTO, (Technicien) personne);
                technicienService.update(personne.getId(), techDTO);
                break;
            case ROLE_TEACHER:
                EnseignantDTO teacherDTO = (EnseignantDTO) personneData;
                entityMapper.updateFromDto(teacherDTO, (Enseignant) personne);
                enseignantService.update(personne.getId(), teacherDTO);
                break;
            case ROLE_STUDENT:
                EtudiantDTO studentDTO = (EtudiantDTO) personneData;
                entityMapper.updateFromDto(studentDTO, (Etudiant) personne);
                etudiantService.update(personne.getId(), studentDTO);
                break;
            default:
                throw new CustomException("Unsupported role: " + role);
        }
    }

    private void deletePersonne(Personne personne, UserRole role) throws CustomException {
        switch (role) {
            case ROLE_ADMIN:
                administrateurService.delete(personne.getId());
                break;
            case ROLE_TECHNICIAN:
                technicienService.delete(personne.getId());
                break;
            case ROLE_TEACHER:
                enseignantService.delete(personne.getId());
                break;
            case ROLE_STUDENT:
                etudiantService.delete(personne.getId());
                break;
            default:
                throw new CustomException("Unsupported role: " + role);
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "unknown";
    }
}