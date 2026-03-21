package itu.m2.ws.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livraisons")
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "commande_id", unique = true, nullable = false)
    private Commande commande;
    @ManyToOne
    @JoinColumn(name = "livreur_id", nullable = false)
    private Livreur livreur;
    @ManyToOne
    @JoinColumn(name = "adresse_livraison_id", nullable = false)
    private Adresse adresseLivraison;
    @Column(nullable = false)
    private Timestamp dateLivraisonEstimee;
    private Timestamp dateLivraisonReelle;
    @Column(nullable = false)
    private Timestamp dateCreation;
    @ManyToOne
    @JoinColumn(name = "statut_livraison_id", nullable = false)
    private StatutLivraison statutLivraison;
    @PrePersist
    protected void onCreate() {
        this.dateCreation = Timestamp.valueOf(LocalDateTime.now());
    }
}
