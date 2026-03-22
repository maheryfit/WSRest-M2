package itu.m2.ws.controllers;

import itu.m2.ws.dto.LivraisonDto;
import itu.m2.ws.models.*;
import itu.m2.ws.services.LivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livraisons")
public class LivraisonController {

    @Autowired
    private LivraisonService livraisonService;

    private LivraisonDto convertToDto(Livraison livraison) {
        return new LivraisonDto(
                livraison.getId(),
                livraison.getCommande().getId(),
                livraison.getLivreur() != null ? livraison.getLivreur().getId() : null,
                livraison.getAdresseLivraison().getId(),
                livraison.getDateLivraisonEstimee(),
                livraison.getDateLivraisonReelle(),
                livraison.getStatutLivraison().getId()
        );
    }

    private Livraison convertToEntity(LivraisonDto livraisonDto) {
        Livraison livraison = new Livraison();
        livraison.setDateLivraisonEstimee(livraisonDto.getDateLivraisonEstimee());
        livraison.setDateLivraisonReelle(livraisonDto.getDateLivraisonReelle());

        Commande commande = new Commande();
        commande.setId(livraisonDto.getCommandeId());
        livraison.setCommande(commande);

        if (livraisonDto.getLivreurId() != null) {
            Livreur livreur = new Livreur();
            livreur.setId(livraisonDto.getLivreurId());
            livraison.setLivreur(livreur);
        }

        Adresse adresse = new Adresse();
        adresse.setId(livraisonDto.getAdresseLivraisonId());
        livraison.setAdresseLivraison(adresse);

        StatutLivraison statutLivraison = new StatutLivraison();
        statutLivraison.setId(livraisonDto.getStatutLivraisonId());
        livraison.setStatutLivraison(statutLivraison);

        return livraison;
    }

    @GetMapping
    public List<LivraisonDto> getAllLivraisons() {
        return livraisonService.getAllLivraisons().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivraisonDto> getLivraisonById(@PathVariable Long id) {
        return livraisonService.getLivraisonById(id)
                .map(livraison -> ResponseEntity.ok(convertToDto(livraison)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LivraisonDto createLivraison(@Valid @RequestBody LivraisonDto livraisonDto) {
        Livraison livraison = convertToEntity(livraisonDto);
        return convertToDto(livraisonService.createLivraison(livraison));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivraisonDto> updateLivraison(@PathVariable Long id, @Valid @RequestBody LivraisonDto livraisonDto) {
        Livraison livraison = convertToEntity(livraisonDto);
        return livraisonService.updateLivraison(id, livraison)
                .map(updatedLivraison -> ResponseEntity.ok(convertToDto(updatedLivraison)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        return livraisonService.deleteLivraison(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
