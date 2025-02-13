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
@Table(name = "tps")
public class TP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nb;

    @ManyToOne
    @JoinColumn(name = "td_id")
    private TD td;

    @OneToMany(mappedBy = "tp",fetch = FetchType.EAGER)
    private List<Etudiant> etudiants;

    // Inverse side for many-to-many with Seance
    @ManyToMany(mappedBy = "tps")
    private List<Seance> seances;
}
