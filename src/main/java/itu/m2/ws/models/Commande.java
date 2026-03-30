package itu.m2.ws.models;

import itu.m2.ws.enums.ModePaiement;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statut_commande_id", nullable = false)
    private StatutCommande statutCommande;

    @Column(nullable = false)
    private double montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePaiement modePaiement;

    @Column(name = "date_creation", updatable = false)
    private Timestamp dateCreation;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LigneCommande> lignesCommandes = new ArrayList<>();

    public void calculerMontantTotal() {
        if (lignesCommandes != null) {
            this.montantTotal = lignesCommandes.stream()
                    .mapToDouble(ligne -> ligne.getQuantite() * ligne.getPrixUnitaire())
                    .sum();
        } else {
            this.montantTotal = 0.0;
        }
    }

    @PrePersist
    protected void onCreate() {
        this.dateCreation = Timestamp.valueOf(LocalDateTime.now());
    }
}