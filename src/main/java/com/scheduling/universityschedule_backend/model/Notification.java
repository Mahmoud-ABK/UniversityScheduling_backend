package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"recepteur", "expediteur"})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime date;
    private String type;
    private Boolean isread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepteur_id")
    private Personne recepteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expediteur_id")
    private Personne expediteur;

    public boolean isRead() {
        return isread;
    }
}
