package itu.m2.ws.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonDto {
    private Long id;

    @NotNull(message = "L'identifiant de la commande ne peut pas être nul")
    private Long commandeId;

    private Long livreurId;

    @NotNull(message = "L'identifiant de l'adresse de livraison ne peut pas être nul")
    private Long adresseLivraisonId;

    private Timestamp dateLivraisonEstimee;

    private Timestamp dateLivraisonReelle;

    @NotNull(message = "L'identifiant du statut de livraison ne peut pas être nul")
    private Long statutLivraisonId;
}
