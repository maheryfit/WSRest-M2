package itu.m2.ws.controllers;

import itu.m2.ws.dto.CommandeDto;
import itu.m2.ws.dto.LivreurDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.enums.Role;
import itu.m2.ws.enums.StatutLivreur;
import itu.m2.ws.services.CommandeService;
import itu.m2.ws.services.LivreurService;
import itu.m2.ws.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livreurs")
public class LivreurController {

    @Autowired
    private LivreurService livreurService;

    @Autowired
    private CommandeService commandeService;

    private LivreurDto convertToDto(Livreur livreur) {
        LivreurDto dto = new LivreurDto();
        dto.setId(livreur.getId());
        dto.setNom(livreur.getNom());
        dto.setPrenom(livreur.getPrenom());
        dto.setTelephone(livreur.getTelephone());
        dto.setStatut(livreur.getStatut());
        dto.setEmail(livreur.getUtilisateur().getEmail());
        return dto;
    }

    private CommandeDto convertCommandeToDto(Commande commande) {
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

    private Livreur convertToEntity(LivreurDto livreurDto, Utilisateur utilisateur) {
        Livreur livreur = new Livreur();
        livreur.setNom(livreurDto.getNom());
        livreur.setPrenom(livreurDto.getPrenom());
        livreur.setTelephone(livreurDto.getTelephone());
        livreur.setStatut(livreurDto.getStatut());
        livreur.setUtilisateur(utilisateur);
        return livreur;
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            assert principal != null;
            return principal.toString();
        }
    }

    @GetMapping
    public List<LivreurDto> getAllLivreurs() {
        return livreurService.getAllLivreurs().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreurDto> getLivreurById(@PathVariable Long id) {
        return livreurService.getLivreurById(id)
                .map(livreur -> ResponseEntity.ok(convertToDto(livreur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/commandes")
    public ResponseEntity<List<CommandeDto>> getMyCommandes() {
        String email = getCurrentUserEmail();
        // Here we need to find the Livreur associated with the user
        // Assuming livreurService has a method to find by user email
        // And then fetch the Commandes assigned to this livreur.
        // Return mock list for compilation until implementation is added
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/me/commandes/{id}/accepter")
    public ResponseEntity<CommandeDto> accepterLivraison(@PathVariable Long id) {
        // Need to link Livreur to Commande/Livraison
        // Return mock for now
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/commandes/{id}/en-livraison")
    public ResponseEntity<CommandeDto> enLivraisonCommande(@PathVariable Long id) {
        // Need to update StatutLivraison
        // Return mock for now
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/commandes/{id}/livree")
    public ResponseEntity<CommandeDto> livreeCommande(@PathVariable Long id) {
        // Need to update StatutLivraison and dateLivraisonReelle
        // Return mock for now
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public LivreurDto createLivreur(@Valid @RequestBody LivreurDto livreurDto) {
        Utilisateur newUser = new Utilisateur();
        newUser.setEmail(livreurDto.getEmail());
        newUser.setMotDePasseHash(livreurDto.getMotDePasse()); // Remember to hash in a real app
        newUser.setRole(Role.LIVREUR);

        Livreur livreur = convertToEntity(livreurDto, newUser);
        return convertToDto(livreurService.createLivreur(livreur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivreurDto> updateLivreur(@PathVariable Long id, @Valid @RequestBody LivreurDto livreurDto) {
        return livreurService.getLivreurById(id)
                .map(existingLivreur -> {
                    Utilisateur utilisateurToUpdate = existingLivreur.getUtilisateur();
                    utilisateurToUpdate.setEmail(livreurDto.getEmail());

                    Livreur livreurToUpdate = convertToEntity(livreurDto, utilisateurToUpdate);
                    livreurToUpdate.setId(id);

                    return livreurService.updateLivreur(id, livreurToUpdate)
                            .map(updatedLivreur -> ResponseEntity.ok(convertToDto(updatedLivreur)))
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/me/statut")
    public ResponseEntity<LivreurDto> updateMyStatus(@RequestBody String statutStr) {
        String email = getCurrentUserEmail();
        // Assuming findByEmail exists in service
        // Mock update for now
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        return livreurService.deleteLivreur(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
