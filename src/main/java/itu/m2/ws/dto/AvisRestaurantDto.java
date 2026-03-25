package itu.m2.ws.dto;

import itu.m2.ws.models.AvisRestaurant;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Restaurant;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvisRestaurantDto {
    private Long id;

    @NotNull(message = "L'identifiant du restaurant ne peut pas être nul")
    private Long restaurantId;

    @NotNull(message = "L'identifiant du client ne peut pas être nul")
    private Long clientId;

    @Min(value = 1, message = "La note doit être au minimum de 1")
    @Max(value = 5, message = "La note doit être au maximum de 5")
    private int note;

    private String commentaire;

    private Timestamp dateCreation;

    public static AvisRestaurantDto convertToDto(AvisRestaurant avis) {
        return new AvisRestaurantDto(
                avis.getId(),
                avis.getRestaurant().getId(),
                avis.getClient().getId(),
                avis.getNote(),
                avis.getCommentaire(),
                avis.getDateCreation()
        );
    }

    public static AvisRestaurant convertToEntity(AvisRestaurantDto avisDto, Long restaurantId) {
        AvisRestaurant avis = new AvisRestaurant();
        avis.setNote(avisDto.getNote());
        avis.setCommentaire(avisDto.getCommentaire());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        avis.setRestaurant(restaurant);

        Client client = new Client();
        client.setId(avisDto.getClientId());
        avis.setClient(client);

        return avis;
    }
}
