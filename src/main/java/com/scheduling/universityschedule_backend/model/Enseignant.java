package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "enseignants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"seances", "propositionsDeRattrapage", "signals"})
public class Enseignant extends Personne {
    private String codeEnseignant;
    private int heures;

    @OneToMany(mappedBy = "enseignant", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Seance> seances;

    @OneToMany(mappedBy = "enseignant", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PropositionDeRattrapage> propositionsDeRattrapage;

    @OneToMany(mappedBy = "enseignant", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Signal> signals;
}
