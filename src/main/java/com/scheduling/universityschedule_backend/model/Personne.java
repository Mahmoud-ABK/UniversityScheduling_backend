package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin;
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String adresse;

    @OneToMany(mappedBy = "recepteur", cascade = CascadeType.REMOVE)
    private List<Notification> receivednotifications;
    @OneToMany(mappedBy = "expediteur", cascade = CascadeType.REMOVE)
    private List<Notification> sentnotifications;

}
