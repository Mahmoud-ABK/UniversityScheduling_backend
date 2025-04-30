package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "tps", indexes = {
        @Index(name = "idx_tp_td", columnList = "td_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"etudiants", "seances"})
public class TP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "td_id")
    private TD td;

    @OneToMany(mappedBy = "tp", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Etudiant> etudiants;

    @ManyToMany(mappedBy = "tps", fetch = FetchType.LAZY)
    private List<Seance> seances;
}
