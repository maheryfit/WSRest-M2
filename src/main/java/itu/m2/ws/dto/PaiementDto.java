package itu.m2.ws.dto;

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
}
