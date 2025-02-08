package com.scheduling.universityschedule_backend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "seances")
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jour;
    private String heureDebut;
    private String heureFin;
    private String type; // possible values CR-CI-TD-TP
    private String matiere;

    @Nullable
    private String frequence;
    // Possible values:
    //  - Empty string for weekly sessions (in which case each list below must have exactly one element)
    //  - "1/15" for biweekly sessions (lists can have more than one element to assign different groups)
    //  - A specific session date for catchup sessions

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    // Use a many-to-many relationship for groups, with business logic to enforce
    // one single association for weekly sessions and multiple for biweekly sessions.

    // For Branch: normally one branch; multiple allowed if biweekly.
    @ManyToMany
    @JoinTable(
            name = "seance_branche",
            joinColumns = @JoinColumn(name = "seance_id"),
            inverseJoinColumns = @JoinColumn(name = "branche_id")
    )
    private List<Branche> branches;

    // For TD: normally one TD; multiple allowed if biweekly.
    @ManyToMany
    @JoinTable(
            name = "seance_td",
            joinColumns = @JoinColumn(name = "seance_id"),
            inverseJoinColumns = @JoinColumn(name = "td_id")
    )
    private List<TD> tds;

    // For TP: normally one TP; multiple allowed if biweekly.
    @ManyToMany
    @JoinTable(
            name = "seance_tp",
            joinColumns = @JoinColumn(name = "seance_id"),
            inverseJoinColumns = @JoinColumn(name = "tp_id")
    )
    private List<TP> tps;
}
