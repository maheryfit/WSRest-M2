package itu.m2.ws.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long id;

    @NotNull(message = "L'identifiant de l'utilisateur ne peut pas être nul")
    private Long utilisateurId;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;

    @NotBlank(message = "Le téléphone ne peut pas être vide")
    private String telephone;
}
