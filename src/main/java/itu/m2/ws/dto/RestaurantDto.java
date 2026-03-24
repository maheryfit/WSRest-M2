package itu.m2.ws.dto;

import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.Utilisateur;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RestaurantDto extends UtilisateurCreationDto {
    private Long id;

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

    public static RestaurantDto convertToDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setNom(restaurant.getNom());
        dto.setDescription(restaurant.getDescription());
        dto.setTelephone(restaurant.getTelephone());
        dto.setAdresse(restaurant.getAdresse());
        dto.setVille(restaurant.getVille());
        dto.setLatitude(restaurant.getLatitude());
        dto.setLongitude(restaurant.getLongitude());
        dto.setOuvert(restaurant.isOuvert());
        dto.setNoteMoyenne(restaurant.getNoteMoyenne());
        dto.setEmail(restaurant.getUtilisateur().getEmail());
        return dto;
    }

    public static Restaurant convertToEntity(RestaurantDto restaurantDto, Utilisateur utilisateur) {
        Restaurant restaurant = new Restaurant();
        restaurant.setNom(restaurantDto.getNom());
        restaurant.setDescription(restaurantDto.getDescription());
        restaurant.setTelephone(restaurantDto.getTelephone());
        restaurant.setAdresse(restaurantDto.getAdresse());
        restaurant.setVille(restaurantDto.getVille());
        restaurant.setLatitude(restaurantDto.getLatitude());
        restaurant.setLongitude(restaurantDto.getLongitude());
        restaurant.setOuvert(restaurantDto.isOuvert());
        restaurant.setNoteMoyenne(restaurantDto.getNoteMoyenne());
        restaurant.setUtilisateur(utilisateur);
        return restaurant;
    }
}
