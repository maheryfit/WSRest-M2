package itu.m2.ws.dto;

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
}
