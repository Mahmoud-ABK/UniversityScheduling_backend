package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salles")
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifiant;
    private String type;
    private int capacite;

    @OneToMany(mappedBy = "salle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Seance> seances;

    @ElementCollection
    private List<String> disponibilite;
}
