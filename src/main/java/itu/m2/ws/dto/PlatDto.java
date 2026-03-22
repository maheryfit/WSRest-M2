package itu.m2.ws.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatDto {
    private Long id;

    @NotNull(message = "L'identifiant du restaurant ne peut pas être nul")
    private Long restaurantId;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    private String description;

    @Positive(message = "Le prix doit être un nombre positif")
    private double prix;

    @NotBlank(message = "La catégorie ne peut pas être vide")
    private String categorie;

    private boolean disponible;
}
