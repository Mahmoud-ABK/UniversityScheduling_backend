package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tds", indexes = {
        @Index(name = "idx_td_branche", columnList = "branche_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"tpList", "seances"})
public class TD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int nb;
    private int nbTP;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branche_id")
    private Branche branche;

    @OneToMany(mappedBy = "td", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<TP> tpList;

    @ManyToMany(mappedBy = "tds", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Seance> seances;

    @ManyToMany(mappedBy = "tds" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<PropositionDeRattrapage> propositionDeRattrapages = new ArrayList<>() ;
}
