package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seances", indexes = {
        @Index(name = "idx_seance_jour", columnList = "jour"),
        @Index(name = "idx_seance_time_range", columnList = "heureDebut,heureFin"),
        @Index(name = "idx_seance_enseignant", columnList = "enseignant_id"),
        @Index(name = "idx_seance_salle", columnList = "salle_id"),
        @Index(name = "idx_seance_frequence", columnList = "frequence"),
        @Index(name = "idx_seance_date", columnList = "date")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String matiere;

    @Enumerated(EnumType.STRING)
    private DayOfWeek jour;

    private LocalTime heureDebut;
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    private FrequenceType frequence = FrequenceType.WEEKLY;

    // New field for makeup sessions
    @Nullable
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    @ManyToMany
    @JoinTable(
            name = "seance_branche",
            joinColumns = @JoinColumn(name = "seance_id"),
            inverseJoinColumns = @JoinColumn(name = "branche_id")
    )
    private List<Branche> branches = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "seance_td",
            joinColumns = @JoinColumn(name = "seance_id"),
            inverseJoinColumns = @JoinColumn(name = "td_id")
    )
    private List<TD> tds = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "seance_tp",
            joinColumns = @JoinColumn(name = "seance_id"),
            inverseJoinColumns = @JoinColumn(name = "tp_id")
    )
    private List<TP> tps = new ArrayList<>();
}