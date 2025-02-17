package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "etudiants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"branche", "tp"})
public class Etudiant extends Personne {
    private String matricule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branche_id")
    private Branche branche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tp_id")
    private TP tp;
}
