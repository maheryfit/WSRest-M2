package itu.m2.ws.controllers;

import itu.m2.ws.dto.CommandeDto;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.services.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    private CommandeDto convertToDto(Commande commande) {
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

    private Commande convertToEntity(CommandeDto commandeDto) {
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

    @GetMapping
    public List<CommandeDto> getAllCommandes() {
        return commandeService.getAllCommandes().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Long id) {
        return commandeService.getCommandeById(id)
                .map(commande -> ResponseEntity.ok(convertToDto(commande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CommandeDto createCommande(@Valid @RequestBody CommandeDto commandeDto) {
        Commande commande = convertToEntity(commandeDto);
        return convertToDto(commandeService.createCommande(commande));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeDto> updateCommande(@PathVariable Long id, @Valid @RequestBody CommandeDto commandeDto) {
        Commande commande = convertToEntity(commandeDto);
        return commandeService.updateCommande(id, commande)
                .map(updatedCommande -> ResponseEntity.ok(convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        return commandeService.deleteCommande(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
