package itu.m2.ws.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import itu.m2.ws.dto.CommandeDto;
import itu.m2.ws.models.Commande;
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

@RestController
@RequestMapping("/api/commandes")
@Tag(name = "CLIENT", description = "Endpoints réservés aux clients")
public class CommandeController extends BaseController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private ClientService clientService;

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<CommandeDto>> getMyCommandes() {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> {
                    List<CommandeDto> commandes = commandeService.getCommandesByClientId(client.getId())
                            .stream()
                            .map(CommandeDto::convertToDto)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(commandes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'RESTAURANT', 'LIVREUR', 'ADMIN')")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Long id) {
        return commandeService.getCommandeById(id)
                .map(commande -> ResponseEntity.ok(CommandeDto.convertToDto(commande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public CommandeDto createCommande(@Valid @RequestBody CommandeDto commandeDto) {
        Commande commande = CommandeDto.convertToEntity(commandeDto);
        return CommandeDto.convertToDto(commandeService.createCommande(commande));
    }

    @PostMapping("/{id}/annuler")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<CommandeDto> annulerCommande(@PathVariable Long id) {
        StatutCommande statutAnnuler = new StatutCommande();
        statutAnnuler.setId(1L); 
        
        return commandeService.updateStatutCommande(id, statutAnnuler)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<CommandeDto> updateCommande(@PathVariable Long id, @Valid @RequestBody CommandeDto commandeDto) {
        Commande commande = CommandeDto.convertToEntity(commandeDto);
        return commandeService.updateCommande(id, commande)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        return commandeService.deleteCommande(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
