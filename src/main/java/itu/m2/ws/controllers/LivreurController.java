package itu.m2.ws.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import itu.m2.ws.dto.CommandeDto;
import itu.m2.ws.dto.LivreurDto;
import itu.m2.ws.enums.Role;
import itu.m2.ws.models.StatutLivraison;
import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.LivraisonService;
import itu.m2.ws.services.LivreurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livreurs")
@Tag(name = "LIVREUR", description = "Endpoints réservés aux livreurs (gestion des livraisons, statut)")
public class LivreurController extends BaseController {

    @Autowired
    private LivreurService livreurService;

    @Autowired
    private LivraisonService livraisonService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<LivreurDto> getAllLivreurs() {
        return livreurService.getAllLivreurs().stream().map(LivreurDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIVREUR')")
    public ResponseEntity<LivreurDto> getLivreurById(@PathVariable Long id) {
        return livreurService.getLivreurById(id)
                .map(livreur -> ResponseEntity.ok(LivreurDto.convertToDto(livreur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/commandes")
    @PreAuthorize("hasAnyRole('LIVREUR')")
    public ResponseEntity<List<CommandeDto>> getMyCommandes() {
        String email = getCurrentUserEmail();
        return livreurService.getLivreurByEmail(email)
                .map(livreur -> {
                    List<CommandeDto> commandes = livraisonService.getLivraisonsByLivreurId(livreur.getId())
                            .stream()
                            .map(livraison -> CommandeDto.convertToDto(livraison.getCommande()))
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(commandes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/accepter")
    @PreAuthorize("hasAnyRole('LIVREUR')")
    public ResponseEntity<CommandeDto> accepterLivraison(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        return livreurService.getLivreurByEmail(email)
                .flatMap(livreur -> livraisonService.getLivraisonByCommandeId(id)
                        .flatMap(livraison -> {
                            livraison.setLivreur(livreur);
                            StatutLivraison statut = new StatutLivraison();
                            statut.setId(2L); // 2: ACCEPTEE_LIVREUR
                            livraison.setStatutLivraison(statut);
                            return livraisonService.updateLivraison(livraison.getId(), livraison);
                        }))
                .map(updatedLivraison -> ResponseEntity.ok(CommandeDto.convertToDto(updatedLivraison.getCommande())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/en-livraison")
    @PreAuthorize("hasAnyRole('LIVREUR')")
    public ResponseEntity<CommandeDto> enLivraisonCommande(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        return livreurService.getLivreurByEmail(email)
                .flatMap(livreur -> livraisonService.getLivraisonByCommandeId(id)
                        .filter(l -> l.getLivreur() != null && l.getLivreur().getId().equals(livreur.getId()))
                        .flatMap(livraison -> {
                            StatutLivraison statut = new StatutLivraison();
                            statut.setId(3L); // 3: EN_LIVRAISON
                            livraison.setStatutLivraison(statut);
                            return livraisonService.updateLivraison(livraison.getId(), livraison);
                        }))
                .map(updatedLivraison -> ResponseEntity.ok(CommandeDto.convertToDto(updatedLivraison.getCommande())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/livree")
    @PreAuthorize("hasAnyRole('LIVREUR')")
    public ResponseEntity<CommandeDto> livreeCommande(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        return livreurService.getLivreurByEmail(email)
                .flatMap(livreur -> livraisonService.getLivraisonByCommandeId(id)
                        .filter(l -> l.getLivreur() != null && l.getLivreur().getId().equals(livreur.getId()))
                        .flatMap(livraison -> {
                            StatutLivraison statut = new StatutLivraison();
                            statut.setId(4L); // 4: LIVREE
                            livraison.setStatutLivraison(statut);
                            livraison.setDateLivraisonReelle(new java.sql.Timestamp(System.currentTimeMillis()));
                            return livraisonService.updateLivraison(livraison.getId(), livraison);
                        }))
                .map(updatedLivraison -> ResponseEntity.ok(CommandeDto.convertToDto(updatedLivraison.getCommande())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LivreurDto createLivreur(@Valid @RequestBody LivreurDto livreurDto) {
        Utilisateur newUser = new Utilisateur();
        newUser.setEmail(livreurDto.getEmail());
        newUser.setMotDePasseHash(livreurDto.getMotDePasse()); // Remember to hash in a real app
        newUser.setRole(Role.LIVREUR);

        Livreur livreur = LivreurDto.convertToEntity(livreurDto, newUser);
        return LivreurDto.convertToDto(livreurService.createLivreur(livreur));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIVREUR')")
    public ResponseEntity<LivreurDto> updateLivreur(@PathVariable Long id, @Valid @RequestBody LivreurDto livreurDto) {
        return livreurService.getLivreurById(id)
                .map(existingLivreur -> {
                    Utilisateur utilisateurToUpdate = existingLivreur.getUtilisateur();
                    utilisateurToUpdate.setEmail(livreurDto.getEmail());

                    Livreur livreurToUpdate = LivreurDto.convertToEntity(livreurDto, utilisateurToUpdate);
                    livreurToUpdate.setId(id);

                    return livreurService.updateLivreur(id, livreurToUpdate)
                            .map(updatedLivreur -> ResponseEntity.ok(LivreurDto.convertToDto(updatedLivreur)))
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/me/statut")
    @PreAuthorize("hasAnyRole('LIVREUR')")
    public ResponseEntity<LivreurDto> updateMyStatus(@RequestBody Map<String, String> body) {
        String email = getCurrentUserEmail();
        String statut = body.get("statut");
        if (statut == null) {
            return ResponseEntity.badRequest().build();
        }
        return livreurService.getLivreurByEmail(email)
                .flatMap(livreur -> livreurService.updateStatus(livreur.getId(), statut))
                .map(updatedLivreur -> ResponseEntity.ok(LivreurDto.convertToDto(updatedLivreur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        return livreurService.deleteLivreur(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
