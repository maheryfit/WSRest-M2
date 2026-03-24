package itu.m2.ws.dto;

import itu.m2.ws.enums.StatutLivreur;
import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.Utilisateur;
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

    public static LivreurDto convertToDto(Livreur livreur) {
        LivreurDto dto = new LivreurDto();
        dto.setId(livreur.getId());
        dto.setNom(livreur.getNom());
        dto.setPrenom(livreur.getPrenom());
        dto.setTelephone(livreur.getTelephone());
        dto.setStatut(livreur.getStatut());
        dto.setEmail(livreur.getUtilisateur().getEmail());
        return dto;
    }

    public static Livreur convertToEntity(LivreurDto livreurDto, Utilisateur utilisateur) {
        Livreur livreur = new Livreur();
        livreur.setNom(livreurDto.getNom());
        livreur.setPrenom(livreurDto.getPrenom());
        livreur.setTelephone(livreurDto.getTelephone());
        livreur.setStatut(livreurDto.getStatut());
        livreur.setUtilisateur(utilisateur);
        return livreur;
    }
}
