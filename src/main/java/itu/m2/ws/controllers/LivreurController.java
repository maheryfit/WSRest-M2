package itu.m2.ws.controllers;

import itu.m2.ws.dto.LivreurDto;
import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.LivreurService;
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
        return new LivreurDto(
                livreur.getId(),
                livreur.getUtilisateur().getId(),
                livreur.getNom(),
                livreur.getPrenom(),
                livreur.getTelephone(),
                livreur.getStatut()
        );
    }

    private Livreur convertToEntity(LivreurDto livreurDto) {
        Livreur livreur = new Livreur();
        livreur.setNom(livreurDto.getNom());
        livreur.setPrenom(livreurDto.getPrenom());
        livreur.setTelephone(livreurDto.getTelephone());
        livreur.setStatut(livreurDto.getStatut());

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(livreurDto.getUtilisateurId());
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
        Livreur livreur = convertToEntity(livreurDto);
        return convertToDto(livreurService.createLivreur(livreur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivreurDto> updateLivreur(@PathVariable Long id, @Valid @RequestBody LivreurDto livreurDto) {
        Livreur livreur = convertToEntity(livreurDto);
        return livreurService.updateLivreur(id, livreur)
                .map(updatedLivreur -> ResponseEntity.ok(convertToDto(updatedLivreur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        return livreurService.deleteLivreur(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
