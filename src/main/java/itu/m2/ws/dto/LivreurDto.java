package itu.m2.ws.dto;

import itu.m2.ws.enums.StatutLivreur;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LivreurDto extends UtilisateurCreationDto {
    private Long id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;

    @NotBlank(message = "Le téléphone ne peut pas être vide")
    private String telephone;

    @NotNull(message = "Le statut ne peut pas être nul")
    private StatutLivreur statut;
}
