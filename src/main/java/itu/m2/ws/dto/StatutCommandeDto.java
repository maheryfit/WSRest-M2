package itu.m2.ws.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutCommandeDto {
    private Long id;

    @NotBlank(message = "Le libellé ne peut pas être vide")
    private String libelle;

    @PositiveOrZero(message = "Le rang doit être un nombre positif ou nul")
    private int rang;
}
