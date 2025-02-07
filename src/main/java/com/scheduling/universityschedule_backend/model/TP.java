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

    @OneToMany(mappedBy = "tp")
    private List<Etudiant> etudiants;
}
