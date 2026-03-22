package itu.m2.ws.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lignes_commandes")
public class LigneCommande {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;
    @ManyToOne @JoinColumn(name = "plat_id")
    private Plat plat;
    private int quantite;
    private double prixUnitaire;
}