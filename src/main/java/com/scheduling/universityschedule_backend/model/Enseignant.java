package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "enseignant",fetch = FetchType.EAGER)
    private List<Seance> seances;

    @OneToMany(mappedBy = "enseignant" ,fetch = FetchType.EAGER)
    private List<PropositionDeRattrapage> propositionsDeRattrapage;
}
