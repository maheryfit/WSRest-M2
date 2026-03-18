package itu.m2.ws.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Table(name = "statuts_commande")
@Data @Entity @Builder @NoArgsConstructor @AllArgsConstructor
public class StatutCommande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String libelle;
    @Column(nullable = false, unique = true)
    @PositiveOrZero
    private int rang; // 0: ANNULER, 1: CREER, etc.
}
