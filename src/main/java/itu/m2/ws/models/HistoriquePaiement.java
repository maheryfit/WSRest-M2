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
@Table(name = "historique_paiements")
public class HistoriquePaiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "paiement_id", nullable = false)
    private Paiement paiement;
    @Column(nullable = false)
    private Timestamp dateStatus;
    @ManyToOne
    @JoinColumn(name = "statut_paiement_id", nullable = false)
    private StatutPaiement statutPaiement;

    @PrePersist
    protected void onCreate() {
        this.dateStatus = Timestamp.valueOf(LocalDateTime.now());
    }
}
