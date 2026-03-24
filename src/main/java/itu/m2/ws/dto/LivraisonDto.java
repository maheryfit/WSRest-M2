package itu.m2.ws.dto;

import itu.m2.ws.models.Adresse;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Livraison;
import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.StatutLivraison;
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

    public static LivraisonDto convertToDto(Livraison livraison) {
        return new LivraisonDto(
                livraison.getId(),
                livraison.getCommande().getId(),
                livraison.getLivreur() != null ? livraison.getLivreur().getId() : null,
                livraison.getAdresseLivraison().getId(),
                livraison.getDateLivraisonEstimee(),
                livraison.getDateLivraisonReelle(),
                livraison.getStatutLivraison().getId()
        );
    }

    public static Livraison convertToEntity(LivraisonDto livraisonDto) {
        Livraison livraison = new Livraison();
        livraison.setDateLivraisonEstimee(livraisonDto.getDateLivraisonEstimee());
        livraison.setDateLivraisonReelle(livraisonDto.getDateLivraisonReelle());

        Commande commande = new Commande();
        commande.setId(livraisonDto.getCommandeId());
        livraison.setCommande(commande);

        if (livraisonDto.getLivreurId() != null) {
            Livreur livreur = new Livreur();
            livreur.setId(livraisonDto.getLivreurId());
            livraison.setLivreur(livreur);
        }

        Adresse adresse = new Adresse();
        adresse.setId(livraisonDto.getAdresseLivraisonId());
        livraison.setAdresseLivraison(adresse);

        StatutLivraison statutLivraison = new StatutLivraison();
        statutLivraison.setId(livraisonDto.getStatutLivraisonId());
        livraison.setStatutLivraison(statutLivraison);

        return livraison;
    }
}
