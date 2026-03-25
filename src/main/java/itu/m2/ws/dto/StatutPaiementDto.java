package itu.m2.ws.dto;

import itu.m2.ws.models.StatutPaiement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutPaiementDto {
    private Long id;

    @NotBlank(message = "Le libellé ne peut pas être vide")
    private String libelle;

    @PositiveOrZero(message = "Le rang doit être un nombre positif ou nul")
    private int rang;

    public static StatutPaiementDto convertToDto(StatutPaiement statut) {
        return new StatutPaiementDto(statut.getId(), statut.getLibelle(), statut.getRang());
    }

    public static StatutPaiement convertToEntity(StatutPaiementDto statutDto) {
        StatutPaiement statut = new StatutPaiement();
        statut.setLibelle(statutDto.getLibelle());
        statut.setRang(statutDto.getRang());
        return statut;
    }
}
