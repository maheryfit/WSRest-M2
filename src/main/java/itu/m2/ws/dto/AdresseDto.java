package itu.m2.ws.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdresseDto {
    private Long id;

    @NotNull(message = "L'identifiant du client ne peut pas être nul")
    private Long clientId;

    @NotBlank(message = "Le libellé ne peut pas être vide")
    private String libelle;

    @NotBlank(message = "La rue ne peut pas être vide")
    private String rue;

    @NotBlank(message = "La ville ne peut pas être vide")
    private String ville;

    @NotBlank(message = "Le code postal ne peut pas être vide")
    private String codePostal;

    @NotNull(message = "La latitude ne peut pas être nulle")
    private Double latitude;

    @NotNull(message = "La longitude ne peut pas être nulle")
    private Double longitude;

    private boolean parDefaut;
}
