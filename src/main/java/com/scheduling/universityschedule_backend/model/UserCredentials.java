package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.scheduling.universityschedule_backend.model.enums.UserRole;
import com.scheduling.universityschedule_backend.model.enums.UserStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_credentials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "failed_attempts")
    private Integer failedAttempts;

    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (failedAttempts == null) {
            failedAttempts = 0;
        }
        if (status == null) {
            status = UserStatus.ACTIVE;
        }
        if (lastPasswordChange == null) {
            lastPasswordChange = LocalDateTime.now();
        }
    }
}