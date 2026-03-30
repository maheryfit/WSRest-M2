package itu.m2.ws.dto;

import itu.m2.ws.models.Commande;
import itu.m2.ws.models.LigneCommande;
import itu.m2.ws.models.Plat;
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

    private Double prixUnitaire;

    public static LigneCommandeDto convertToDto(LigneCommande ligneCommande) {
        return new LigneCommandeDto(
                ligneCommande.getId(),
                ligneCommande.getCommande().getId(),
                ligneCommande.getPlat().getId(),
                ligneCommande.getQuantite(),
                ligneCommande.getPrixUnitaire());
    }

    public static LigneCommande convertToEntity(LigneCommandeDto ligneCommandeDto) {
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setQuantite(ligneCommandeDto.getQuantite());
        if (ligneCommandeDto.getPrixUnitaire() != null) {
            ligneCommande.setPrixUnitaire(ligneCommandeDto.getPrixUnitaire());
        } else {
            ligneCommande.setPrixUnitaire(0.0);
        }

        Commande commande = new Commande();
        commande.setId(ligneCommandeDto.getCommandeId());
        ligneCommande.setCommande(commande);

        Plat plat = new Plat();
        plat.setId(ligneCommandeDto.getPlatId());
        ligneCommande.setPlat(plat);

        return ligneCommande;
    }
}
