package itu.m2.ws.dto;

import itu.m2.ws.models.HistoriqueLivraison;
import itu.m2.ws.models.Livraison;
import itu.m2.ws.models.StatutLivraison;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueLivraisonDto {
    private Long id;

    @NotNull(message = "L'identifiant de la livraison ne peut pas être nul")
    private Long livraisonId;

    private Timestamp dateStatus;

    @NotNull(message = "L'identifiant du statut de livraison ne peut pas être nul")
    private Long statutLivraisonId;

    public static HistoriqueLivraisonDto convertToDto(HistoriqueLivraison historique) {
        return new HistoriqueLivraisonDto(
                historique.getId(),
                historique.getLivraison().getId(),
                historique.getDateStatus(),
                historique.getStatutLivraison().getId()
        );
    }

    public static HistoriqueLivraison convertToEntity(HistoriqueLivraisonDto historiqueDto) {
        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setDateStatus(historiqueDto.getDateStatus());

        Livraison livraison = new Livraison();
        livraison.setId(historiqueDto.getLivraisonId());
        historique.setLivraison(livraison);

        StatutLivraison statut = new StatutLivraison();
        statut.setId(historiqueDto.getStatutLivraisonId());
        historique.setStatutLivraison(statut);

        return historique;
    }
}
