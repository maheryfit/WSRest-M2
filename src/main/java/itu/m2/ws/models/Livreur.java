package itu.m2.ws.models;

import itu.m2.ws.enums.StatutLivreur;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "livreurs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nom", "prenom"})
})
@Data @Entity @Builder @NoArgsConstructor @AllArgsConstructor
public class Livreur {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String prenom;
    @Column(nullable = false, unique = true)
    private String telephone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutLivreur statut;
}
