package itu.m2.ws.dto;

import itu.m2.ws.enums.ModePaiement;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.StatutCommande;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CommandeDto extends RepresentationModel<CommandeDto> {
    private Long id;

    private Long clientId;

    @NotNull(message = "L'identifiant du restaurant ne peut pas être nul")
    private Long restaurantId;

    private Long statutCommandeId = 1L;

    private Double montantTotal;

    @NotNull(message = "Le mode de paiement ne peut pas être nul")
    private ModePaiement modePaiement;

    private List<LigneCommandeDto> lignesCommandes;

    private Timestamp dateCreation;

    public static CommandeDto convertToDto(Commande commande) {
        return new CommandeDto(
                commande.getId(),
                commande.getClient().getId(),
                commande.getRestaurant().getId(),
                commande.getStatutCommande().getId(),
                commande.getMontantTotal(),
                commande.getModePaiement(),
                commande.getLignesCommandes() != null
                        ? commande.getLignesCommandes().stream().map(LigneCommandeDto::convertToDto)
                                .collect(Collectors.toList())
                        : null,
                commande.getDateCreation());
    }

    public static Commande convertToEntity(CommandeDto commandeDto) {
        Commande commande = new Commande();
        if (commandeDto.getMontantTotal() != null) {
            commande.setMontantTotal(commandeDto.getMontantTotal());
        } else {
            commande.setMontantTotal(0.0);
        }
        commande.setModePaiement(commandeDto.getModePaiement());

        Client client = new Client();
        client.setId(commandeDto.getClientId());
        commande.setClient(client);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(commandeDto.getRestaurantId());
        commande.setRestaurant(restaurant);

        StatutCommande statutCommande = new StatutCommande();
        statutCommande.setId(commandeDto.getStatutCommandeId() != null ? commandeDto.getStatutCommandeId() : 1L);
        commande.setStatutCommande(statutCommande);

        if (commandeDto.getLignesCommandes() != null) {
            commande.setLignesCommandes(commandeDto.getLignesCommandes().stream()
                    .map(LigneCommandeDto::convertToEntity)
                    .collect(Collectors.toList()));
            commande.getLignesCommandes().forEach(ligne -> ligne.setCommande(commande));
        }

        return commande;
    }
}
