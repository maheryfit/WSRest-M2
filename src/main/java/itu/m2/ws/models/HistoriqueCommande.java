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
@Table(name = "historique_commandes")
public class HistoriqueCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;
    @Column(nullable = false)
    private Timestamp dateStatus;
    @ManyToOne
    @JoinColumn(name = "statut_commande_id", nullable = false)
    private StatutCommande statutCommande;

    @PrePersist
    protected void onCreate() {
        this.dateStatus = Timestamp.valueOf(LocalDateTime.now());
    }
}
