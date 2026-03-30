package itu.m2.ws.dto;

import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.StatutPaiement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDto {
    private Long id;

    @NotNull(message = "L'identifiant de la commande ne peut pas être nul")
    private Long commandeId;

    @Positive(message = "Le montant doit être un nombre positif")
    private double montant;

    @NotNull(message = "L'identifiant du statut de paiement ne peut pas être nul")
    private Long statutPaiementId;

    private Timestamp datePaiement;

    public static PaiementDto convertToDto(Paiement paiement) {
        return new PaiementDto(
                paiement.getId(),
                paiement.getCommande().getId(),
                paiement.getMontant(),
                paiement.getStatutPaiement().getId(),
                paiement.getDatePaiement());
    }

    public static Paiement convertToEntity(PaiementDto paiementDto) {
        Paiement paiement = new Paiement();
        paiement.setMontant(paiementDto.getMontant());
        paiement.setDatePaiement(paiementDto.getDatePaiement());

        Commande commande = new Commande();
        commande.setId(paiementDto.getCommandeId());
        paiement.setCommande(commande);

        StatutPaiement statutPaiement = new StatutPaiement();
        statutPaiement.setId(paiementDto.getStatutPaiementId());
        paiement.setStatutPaiement(statutPaiement);

        return paiement;
    }
}
