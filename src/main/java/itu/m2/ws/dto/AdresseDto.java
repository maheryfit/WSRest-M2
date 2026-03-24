package itu.m2.ws.dto;

import itu.m2.ws.models.Adresse;
import itu.m2.ws.models.Client;
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

    public static AdresseDto convertToDto(Adresse adresse) {
        return new AdresseDto(
                adresse.getId(),
                adresse.getClient().getId(),
                adresse.getLibelle(),
                adresse.getRue(),
                adresse.getVille(),
                adresse.getCodePostal(),
                adresse.getLatitude(),
                adresse.getLongitude(),
                adresse.isParDefaut()
        );
    }

    public static Adresse convertToEntity(AdresseDto adresseDto) {
        Adresse adresse = new Adresse();
        adresse.setLibelle(adresseDto.getLibelle());
        adresse.setRue(adresseDto.getRue());
        adresse.setVille(adresseDto.getVille());
        adresse.setCodePostal(adresseDto.getCodePostal());
        adresse.setLatitude(adresseDto.getLatitude());
        adresse.setLongitude(adresseDto.getLongitude());
        adresse.setParDefaut(adresseDto.isParDefaut());

        Client client = new Client();
        client.setId(adresseDto.getClientId());
        adresse.setClient(client);

        return adresse;
    }
}
