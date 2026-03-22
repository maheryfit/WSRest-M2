package itu.m2.ws.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurCreationDto {
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Le format de l'email est invalide")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String motDePasse;
}
