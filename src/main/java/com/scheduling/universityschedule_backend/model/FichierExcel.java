package com.scheduling.universityschedule_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fichiers_excel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichierExcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String status;

    @ElementCollection
    private List<String> errors;

    private LocalDateTime importDate;
}
