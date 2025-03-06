package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "propositions_de_rattrapage", indexes = {
        @Index(name = "idx_proposition_date", columnList = "date"),
        @Index(name = "idx_proposition_status", columnList = "status"),
        @Index(name = "idx_proposition_type", columnList = "type"),
        @Index(name = "idx_proposition_enseignant", columnList = "enseignant_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"enseignant", "branches", "tds", "tps"})
public class PropositionDeRattrapage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String matiere;

    @Enumerated(EnumType.STRING)
    private SeanceType type;  // New field for session type

    private LocalTime heureDebut;
    private LocalTime heureFin;
    private LocalDateTime date;

    private String reason;
    private Status status =Status.PENDING;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "proposition_branche",
            joinColumns = @JoinColumn(name = "proposition_id"),
            inverseJoinColumns = @JoinColumn(name = "branche_id")
    )
    private List<Branche> branches = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "proposition_td",
            joinColumns = @JoinColumn(name = "proposition_id"),
            inverseJoinColumns = @JoinColumn(name = "td_id")
    )
    private List<TD> tds = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "proposition_tp",
            joinColumns = @JoinColumn(name = "proposition_id"),
            inverseJoinColumns = @JoinColumn(name = "tp_id")
    )
    private List<TP> tps = new ArrayList<>();
}