package com.scheduling.universityschedule_backend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "seances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"branches", "tds", "tps", "salle", "enseignant"})
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jour;
    private String heureDebut;
    private String heureFin;
    private String type;
    private String matiere;

    @Nullable
    private String frequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "seance_branche",
        joinColumns = @JoinColumn(name = "seance_id"),
        inverseJoinColumns = @JoinColumn(name = "branche_id")
    )
    private List<Branche> branches;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "seance_td",
        joinColumns = @JoinColumn(name = "seance_id"),
        inverseJoinColumns = @JoinColumn(name = "td_id")
    )
    private List<TD> tds;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "seance_tp",
        joinColumns = @JoinColumn(name = "seance_id"),
        inverseJoinColumns = @JoinColumn(name = "tp_id")
    )
    private List<TP> tps;

    @Override
    public String toString() {
        return "Seance{id=" + id +
                ", jour='" + jour + '\'' +
                ", heureDebut='" + heureDebut + '\'' +
                ", heureFin='" + heureFin + '\'' +
                ", type='" + type + '\'' +
                ", matiere='" + matiere + '\'' +
                ", frequence='" + frequence + '\'' +
                ", salleId=" + (salle != null ? salle.getId() : "N/A") +
                ", enseignantId=" + (enseignant != null ? enseignant.getId() : "N/A") +
                ", branchesCount=" + (branches != null ? branches.size() : "N/A") +
                ", tdsCount=" + (tds != null ? tds.size() : "N/A") +
                ", tpsCount=" + (tps != null ? tps.size() : "N/A") +
                '}';
    }
}
