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
@Table(name = "tds")
public class TD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nb;
    private int nbTP;

    @ManyToOne
    @JoinColumn(name = "branche_id")
    private Branche branche;

    @OneToMany(mappedBy = "td",fetch = FetchType.EAGER)
    private List<TP> tpList;

    // Inverse side for many-to-many with Seance
    @ManyToMany(mappedBy = "tds",fetch = FetchType.EAGER)
    private List<Seance> seances;
}
