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
@Table(name = "historique_livraisons")
public class HistoriqueLivraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "livraison_id", nullable = false)
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Livraison livraison;
    @Column(nullable = false)
    private Timestamp dateStatus;
    @ManyToOne
    @JoinColumn(name = "statut_livraison_id", nullable = false)
    private StatutLivraison statutLivraison;

    @PrePersist
    protected void onCreate() {
        this.dateStatus = Timestamp.valueOf(LocalDateTime.now());
    }
}
