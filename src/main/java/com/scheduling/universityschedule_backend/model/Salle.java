package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "salles", indexes = {
        @Index(name = "idx_salle_identifiant", columnList = "identifiant"),
        @Index(name = "idx_salle_type", columnList = "type"),
        @Index(name = "idx_salle_capacite", columnList = "capacite")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
    name = "Salle.withSeances",
    attributeNodes = @NamedAttributeNode("seances")
)
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifiant;
    private String type;
    private int capacite;

    @OneToMany(mappedBy = "salle", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Seance> seances;



    @Override
    public String toString() {
        return "Salle{id=" + id +
                ", identifiant='" + identifiant + '\'' +
                ", type='" + type + '\'' +
                ", capacite=" + capacite +
                ", seancesCount=" + (seances != null ? seances.size() : "N/A") +
                '}';
    }
}
