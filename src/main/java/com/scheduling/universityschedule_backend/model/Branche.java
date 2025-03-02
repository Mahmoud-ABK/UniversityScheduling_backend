package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    @ManyToMany(mappedBy = "branches", fetch = FetchType.LAZY)
    private List<Seance> seances;
}
