package itu.m2.ws.dto;

import itu.m2.ws.enums.StatutLivreur;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivreurDto {
    private Long id;

    @NotNull(message = "L'identifiant de l'utilisateur ne peut pas être nul")
    private Long utilisateurId;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;

    @NotBlank(message = "Le téléphone ne peut pas être vide")
    private String telephone;

    @NotNull(message = "Le statut ne peut pas être nul")
    private StatutLivreur statut;
}
