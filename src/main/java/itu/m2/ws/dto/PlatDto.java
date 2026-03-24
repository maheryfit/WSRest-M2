package itu.m2.ws.dto;

import itu.m2.ws.models.Plat;
import itu.m2.ws.models.Restaurant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatDto {
    private Long id;

    @NotNull(message = "L'identifiant du restaurant ne peut pas être nul")
    private Long restaurantId;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    private String description;

    @Positive(message = "Le prix doit être un nombre positif")
    private double prix;

    @NotBlank(message = "La catégorie ne peut pas être vide")
    private String categorie;

    private boolean disponible;

    public static PlatDto convertToDto(Plat plat) {
        return new PlatDto(
                plat.getId(),
                plat.getRestaurant().getId(),
                plat.getNom(),
                plat.getDescription(),
                plat.getPrix(),
                plat.getCategorie(),
                plat.isDisponible()
        );
    }

    public static Plat convertToEntity(PlatDto platDto, Long restaurantId) {
        Plat plat = new Plat();
        plat.setNom(platDto.getNom());
        plat.setDescription(platDto.getDescription());
        plat.setPrix(platDto.getPrix());
        plat.setCategorie(platDto.getCategorie());
        plat.setDisponible(platDto.isDisponible());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        plat.setRestaurant(restaurant);

        return plat;
    }
}
