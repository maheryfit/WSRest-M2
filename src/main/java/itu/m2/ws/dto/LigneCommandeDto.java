package itu.m2.ws.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeDto {
    private Long id;

    @NotNull(message = "L'identifiant de la commande ne peut pas être nul")
    private Long commandeId;

    @NotNull(message = "L'identifiant du plat ne peut pas être nul")
    private Long platId;

    @Positive(message = "La quantité doit être un nombre positif")
    private int quantite;

    @Positive(message = "Le prix unitaire doit être un nombre positif")
    private double prixUnitaire;
}
