package itu.m2.ws.controllers;

import itu.m2.ws.dto.LivreurDto;
import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.LivreurService;
import itu.m2.ws.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livreurs")
public class LivreurController {

    @Autowired
    private LivreurService livreurService;

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

    private Livreur convertToEntity(LivreurDto livreurDto, Utilisateur utilisateur) {
        Livreur livreur = new Livreur();
        livreur.setNom(livreurDto.getNom());
        livreur.setPrenom(livreurDto.getPrenom());
        livreur.setTelephone(livreurDto.getTelephone());
        livreur.setStatut(livreurDto.getStatut());
        livreur.setUtilisateur(utilisateur);
        return livreur;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        return livreurService.deleteLivreur(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
