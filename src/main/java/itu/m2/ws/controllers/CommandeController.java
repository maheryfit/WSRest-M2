package itu.m2.ws.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import itu.m2.ws.dto.CommandeDto;
import itu.m2.ws.dto.LigneCommandeDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.LigneCommande;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.services.ClientService;
import itu.m2.ws.services.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/commandes")
@Tag(name = "CLIENT", description = "Endpoints réservés aux clients")
@SecurityRequirement(name = "bearerAuth")
public class CommandeController extends BaseController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<List<CommandeDto>> getMyCommandes() {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> {
                    List<CommandeDto> commandes = commandeService.getCommandesByClientId(client.getId())
                            .stream()
                            .map(this::addHateoasLinks)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(commandes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'RESTAURANT', 'LIVREUR', 'ADMIN')")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Long id) {
        return commandeService.getCommandeById(id)
                .map(this::addHateoasLinks)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private CommandeDto addHateoasLinks(Commande commande) {
        CommandeDto dto = CommandeDto.convertToDto(commande);

        // self
        dto.add(linkTo(methodOn(CommandeController.class).getCommandeById(commande.getId())).withSelfRel());

        // restaurant
        dto.add(linkTo(methodOn(RestaurantController.class).getRestaurantById(commande.getRestaurant().getId()))
                .withRel("restaurant"));

        // client
        dto.add(linkTo(methodOn(ClientController.class).getClientById(commande.getClient().getId())).withRel("client"));

        // next-actions based on status
        String status = commande.getStatutCommande().getLibelle();
        if ("CREER".equals(status)) {
            dto.add(linkTo(methodOn(CommandeController.class).annulerCommande(commande.getId())).withRel("annuler"));
            dto.add(linkTo(methodOn(RestaurantController.class).accepterCommande(commande.getId()))
                    .withRel("accepter"));
        } else if ("LIVREE".equals(status)) {
            dto.add(linkTo(methodOn(AvisRestaurantController.class)
                    .createAvisByRestaurantId(commande.getRestaurant().getId(), null)).withRel("noter"));
        }

        return dto;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT')")
    public CommandeDto createCommande(@Valid @RequestBody CommandeDto commandeDto) {
        Commande commande = CommandeDto.convertToEntity(commandeDto);
        return addHateoasLinks(commandeService.createCommande(commande));
    }

    @PostMapping("/{id}/annuler")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<CommandeDto> annulerCommande(@PathVariable Long id) {
        StatutCommande statutAnnuler = new StatutCommande();
        statutAnnuler.setId(1L);

        return commandeService.updateStatutCommande(id, statutAnnuler)
                .map(updatedCommande -> ResponseEntity.ok(addHateoasLinks(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<CommandeDto> updateCommande(@PathVariable Long id,
            @Valid @RequestBody CommandeDto commandeDto) {
        Commande commande = CommandeDto.convertToEntity(commandeDto);
        return commandeService.updateCommande(id, commande)
                .map(updatedCommande -> ResponseEntity.ok(addHateoasLinks(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        return commandeService.deleteCommande(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/lignes")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<CommandeDto> ajouterLigne(@PathVariable Long id,
            @Valid @RequestBody LigneCommandeDto ligneDto) {
        LigneCommande ligne = LigneCommandeDto.convertToEntity(ligneDto);
        return commandeService.ajouterLigneCommande(id, ligne)
                .map(this::addHateoasLinks)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/lignes/{ligneId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<CommandeDto> supprimerLigne(@PathVariable Long id, @PathVariable Long ligneId) {
        return commandeService.supprimerLigneCommande(id, ligneId)
                .map(this::addHateoasLinks)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
