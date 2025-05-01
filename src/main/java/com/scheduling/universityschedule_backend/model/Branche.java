package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "branches", indexes = {
        @Index(name = "idx_branche_niveau_specialite", columnList = "niveau,specialite")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
    name = "Branche.withSeances",
    attributeNodes = @NamedAttributeNode("seances")
)
@ToString(exclude = {"seances"})
public class Branche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String niveau;
    private String specialite;
    private int nbTD;
    private String departement;

    @OneToMany(mappedBy ="branche", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<TD> tds;

    @ManyToMany(mappedBy = "branches" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<PropositionDeRattrapage> propositionDeRattrapages = new ArrayList<>() ;

    @ManyToMany(mappedBy = "branches", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<Seance> seances;
}
