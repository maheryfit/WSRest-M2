package itu.m2.ws.dto;

import itu.m2.ws.enums.ModePaiement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDto {
    private Long id;

    @NotNull(message = "L'identifiant du client ne peut pas être nul")
    private Long clientId;

    @NotNull(message = "L'identifiant du restaurant ne peut pas être nul")
    private Long restaurantId;

    @NotNull(message = "L'identifiant du statut de commande ne peut pas être nul")
    private Long statutCommandeId;

    @Positive(message = "Le montant total doit être un nombre positif")
    private double montantTotal;

    @NotNull(message = "Le mode de paiement ne peut pas être nul")
    private ModePaiement modePaiement;

    private Timestamp dateCreation;
}
