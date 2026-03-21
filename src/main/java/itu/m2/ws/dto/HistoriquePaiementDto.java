package itu.m2.ws.dto;

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
}
