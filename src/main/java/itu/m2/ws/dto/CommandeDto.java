package itu.m2.ws.dto;

import itu.m2.ws.enums.ModePaiement;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.StatutCommande;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CommandeDto extends RepresentationModel<CommandeDto> {
    private Long id;

    @NotNull(message = "L'identifiant du client ne peut pas être nul")
    private Long clientId;

    @NotNull(message = "L'identifiant du restaurant ne peut pas être nul")
    private Long restaurantId;

    @NotNull(message = "L'identifiant du statut de commande ne peut pas être nul")
    private Long statutCommandeId;

    @Positive(message = "Le montant total doit être un nombre positif")
    private Double montantTotal;

    @NotNull(message = "Le mode de paiement ne peut pas être nul")
    private ModePaiement modePaiement;

    private Timestamp dateCreation;

    public static CommandeDto convertToDto(Commande commande) {
        return new CommandeDto(
                commande.getId(),
                commande.getClient().getId(),
                commande.getRestaurant().getId(),
                commande.getStatutCommande().getId(),
                commande.getMontantTotal(),
                commande.getModePaiement(),
                commande.getDateCreation()
        );
    }

    public static Commande convertToEntity(CommandeDto commandeDto) {
        Commande commande = new Commande();
        commande.setMontantTotal(commandeDto.getMontantTotal());
        commande.setModePaiement(commandeDto.getModePaiement());

        Client client = new Client();
        client.setId(commandeDto.getClientId());
        commande.setClient(client);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(commandeDto.getRestaurantId());
        commande.setRestaurant(restaurant);

        StatutCommande statutCommande = new StatutCommande();
        statutCommande.setId(commandeDto.getStatutCommandeId());
        commande.setStatutCommande(statutCommande);

        return commande;
    }
}
