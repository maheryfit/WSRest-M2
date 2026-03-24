package itu.m2.ws.dto;

import itu.m2.ws.models.HistoriquePaiement;
import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.StatutPaiement;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriquePaiementDto {
    private Long id;

    @NotNull(message = "L'identifiant du paiement ne peut pas être nul")
    private Long paiementId;

    private Timestamp dateStatus;

    @NotNull(message = "L'identifiant du statut de paiement ne peut pas être nul")
    private Long statutPaiementId;

    public static HistoriquePaiementDto convertToDto(HistoriquePaiement historique) {
        return new HistoriquePaiementDto(
                historique.getId(),
                historique.getPaiement().getId(),
                historique.getDateStatus(),
                historique.getStatutPaiement().getId()
        );
    }

    public static HistoriquePaiement convertToEntity(HistoriquePaiementDto historiqueDto) {
        HistoriquePaiement historique = new HistoriquePaiement();
        historique.setDateStatus(historiqueDto.getDateStatus());

        Paiement paiement = new Paiement();
        paiement.setId(historiqueDto.getPaiementId());
        historique.setPaiement(paiement);

        StatutPaiement statut = new StatutPaiement();
        statut.setId(historiqueDto.getStatutPaiementId());
        historique.setStatutPaiement(statut);

        return historique;
    }
}
