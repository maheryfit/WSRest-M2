package itu.m2.ws.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
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
@Table(name = "paiements")
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;
    @Positive
    @Column(nullable = false)
    private double montant;
    @ManyToOne
    @JoinColumn(name = "statut_paiement_id", nullable = false)
    private StatutPaiement statutPaiement;
    @Column(nullable = false)
    private Timestamp datePaiement;

    @PrePersist
    protected void onCreate() {
        this.datePaiement = Timestamp.valueOf(LocalDateTime.now());
    }
}
