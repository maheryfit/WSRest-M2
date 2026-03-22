package itu.m2.ws.controllers;

import itu.m2.ws.dto.StatutLivraisonDto;
import itu.m2.ws.models.StatutLivraison;
import itu.m2.ws.services.StatutLivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statutlivraisons")
public class StatutLivraisonController {

    @Autowired
    private StatutLivraisonService statutLivraisonService;

    private StatutLivraisonDto convertToDto(StatutLivraison statut) {
        return new StatutLivraisonDto(statut.getId(), statut.getLibelle(), statut.getRang());
    }

    private StatutLivraison convertToEntity(StatutLivraisonDto statutDto) {
        StatutLivraison statut = new StatutLivraison();
        statut.setLibelle(statutDto.getLibelle());
        statut.setRang(statutDto.getRang());
        return statut;
    }

    @GetMapping
    public List<StatutLivraisonDto> getAllStatutLivraisons() {
        return statutLivraisonService.getAllStatutLivraisons().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatutLivraisonDto> getStatutLivraisonById(@PathVariable Long id) {
        return statutLivraisonService.getStatutLivraisonById(id)
                .map(statut -> ResponseEntity.ok(convertToDto(statut)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public StatutLivraisonDto createStatutLivraison(@Valid @RequestBody StatutLivraisonDto statutDto) {
        StatutLivraison statut = convertToEntity(statutDto);
        return convertToDto(statutLivraisonService.createStatutLivraison(statut));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatutLivraisonDto> updateStatutLivraison(@PathVariable Long id, @Valid @RequestBody StatutLivraisonDto statutDto) {
        StatutLivraison statut = convertToEntity(statutDto);
        return statutLivraisonService.updateStatutLivraison(id, statut)
                .map(updatedStatut -> ResponseEntity.ok(convertToDto(updatedStatut)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatutLivraison(@PathVariable Long id) {
        return statutLivraisonService.deleteStatutLivraison(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
