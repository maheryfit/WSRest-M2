package itu.m2.ws.dto;

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
}
