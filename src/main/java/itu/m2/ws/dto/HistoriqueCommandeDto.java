package itu.m2.ws.dto;

import itu.m2.ws.models.Commande;
import itu.m2.ws.models.HistoriqueCommande;
import itu.m2.ws.models.StatutCommande;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueCommandeDto {
    private Long id;

    @NotNull(message = "L'identifiant de la commande ne peut pas être nul")
    private Long commandeId;

    private Timestamp dateStatus;

    @NotNull(message = "L'identifiant du statut de commande ne peut pas être nul")
    private Long statutCommandeId;

    public static HistoriqueCommandeDto convertToDto(HistoriqueCommande historique) {
        return new HistoriqueCommandeDto(
                historique.getId(),
                historique.getCommande().getId(),
                historique.getDateStatus(),
                historique.getStatutCommande().getId()
        );
    }

    public static HistoriqueCommande convertToEntity(HistoriqueCommandeDto historiqueDto) {
        HistoriqueCommande historique = new HistoriqueCommande();
        historique.setDateStatus(historiqueDto.getDateStatus());

        Commande commande = new Commande();
        commande.setId(historiqueDto.getCommandeId());
        historique.setCommande(commande);

        StatutCommande statut = new StatutCommande();
        statut.setId(historiqueDto.getStatutCommandeId());
        historique.setStatutCommande(statut);

        return historique;
    }
}
