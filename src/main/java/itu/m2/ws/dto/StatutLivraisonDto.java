package itu.m2.ws.dto;

import itu.m2.ws.models.StatutLivraison;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutLivraisonDto {
    private Long id;

    @NotBlank(message = "Le libellé ne peut pas être vide")
    private String libelle;

    @PositiveOrZero(message = "Le rang doit être un nombre positif ou nul")
    private int rang;

    public static StatutLivraisonDto convertToDto(StatutLivraison statut) {
        return new StatutLivraisonDto(statut.getId(), statut.getLibelle(), statut.getRang());
    }

    public static StatutLivraison convertToEntity(StatutLivraisonDto statutDto) {
        StatutLivraison statut = new StatutLivraison();
        statut.setLibelle(statutDto.getLibelle());
        statut.setRang(statutDto.getRang());
        return statut;
    }
}
