package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enseignants")
public class Enseignant extends Personne {
    private String codeEnseignant;
    private int heures;

    @OneToMany(mappedBy = "enseignant")
    private List<Seance> seances;

    @OneToMany(mappedBy = "enseignant")
    private List<PropositionDeRattrapage> propositionsDeRattrapage;
}
