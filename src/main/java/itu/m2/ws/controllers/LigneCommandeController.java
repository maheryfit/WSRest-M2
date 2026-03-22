package itu.m2.ws.controllers;

import itu.m2.ws.dto.LigneCommandeDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.LigneCommande;
import itu.m2.ws.models.Plat;
import itu.m2.ws.services.LigneCommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lignecommandes")
public class LigneCommandeController {

    @Autowired
    private LigneCommandeService ligneCommandeService;

    private LigneCommandeDto convertToDto(LigneCommande ligneCommande) {
        return new LigneCommandeDto(
                ligneCommande.getId(),
                ligneCommande.getCommande().getId(),
                ligneCommande.getPlat().getId(),
                ligneCommande.getQuantite(),
                ligneCommande.getPrixUnitaire()
        );
    }

    private LigneCommande convertToEntity(LigneCommandeDto ligneCommandeDto) {
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setQuantite(ligneCommandeDto.getQuantite());
        ligneCommande.setPrixUnitaire(ligneCommandeDto.getPrixUnitaire());

        Commande commande = new Commande();
        commande.setId(ligneCommandeDto.getCommandeId());
        ligneCommande.setCommande(commande);

        Plat plat = new Plat();
        plat.setId(ligneCommandeDto.getPlatId());
        ligneCommande.setPlat(plat);

        return ligneCommande;
    }

    @GetMapping
    public List<LigneCommandeDto> getAllLigneCommandes() {
        return ligneCommandeService.getAllLigneCommandes().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigneCommandeDto> getLigneCommandeById(@PathVariable Long id) {
        return ligneCommandeService.getLigneCommandeById(id)
                .map(ligneCommande -> ResponseEntity.ok(convertToDto(ligneCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LigneCommandeDto createLigneCommande(@Valid @RequestBody LigneCommandeDto ligneCommandeDto) {
        LigneCommande ligneCommande = convertToEntity(ligneCommandeDto);
        return convertToDto(ligneCommandeService.createLigneCommande(ligneCommande));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LigneCommandeDto> updateLigneCommande(@PathVariable Long id, @Valid @RequestBody LigneCommandeDto ligneCommandeDto) {
        LigneCommande ligneCommande = convertToEntity(ligneCommandeDto);
        return ligneCommandeService.updateLigneCommande(id, ligneCommande)
                .map(updatedLigneCommande -> ResponseEntity.ok(convertToDto(updatedLigneCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigneCommande(@PathVariable Long id) {
        return ligneCommandeService.deleteLigneCommande(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
