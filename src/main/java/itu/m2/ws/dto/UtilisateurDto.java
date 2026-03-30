package itu.m2.ws.dto;

import itu.m2.ws.enums.Role;
import itu.m2.ws.models.Utilisateur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDto {
    private Long id;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Le format de l'email est invalide")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String motDePasse;

    @NotNull(message = "Le rôle ne peut pas être nul")
    private Role role = Role.ADMIN;

    private boolean actif;

    private Timestamp dateCreation;

    public static UtilisateurDto convertToDto(Utilisateur utilisateur) {
        return new UtilisateurDto(
                utilisateur.getId(),
                utilisateur.getEmail(),
                null, // Do not send password back
                utilisateur.getRole(),
                utilisateur.isActif(),
                utilisateur.getDateCreation()
        );
    }

    public static Utilisateur convertToEntity(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(utilisateurDto.getEmail());
        // In a real app, you'd hash the password here before saving
        utilisateur.setMotDePasseHash(utilisateurDto.getMotDePasse());
        utilisateur.setRole(utilisateurDto.getRole());
        utilisateur.setActif(utilisateurDto.isActif());
        return utilisateur;
    }
}
