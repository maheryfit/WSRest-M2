package itu.m2.ws.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;

    @NotNull(message = "L'identifiant de l'utilisateur ne peut pas être nul")
    private Long utilisateurId;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    private String description;

    @NotBlank(message = "Le téléphone ne peut pas être vide")
    private String telephone;

    @NotBlank(message = "L'adresse ne peut pas être vide")
    private String adresse;

    @NotBlank(message = "La ville ne peut pas être vide")
    private String ville;

    @NotNull(message = "La latitude ne peut pas être nulle")
    private Double latitude;

    @NotNull(message = "La longitude ne peut pas être nulle")
    private Double longitude;

    private boolean ouvert;

    @PositiveOrZero(message = "La note moyenne doit être un nombre positif ou nul")
    private double noteMoyenne;
}
