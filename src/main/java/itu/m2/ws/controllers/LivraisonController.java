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

    @GetMapping
    public List<LivraisonDto> getAllLivraisons() {
        return livraisonService.getAllLivraisons().stream().map(LivraisonDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivraisonDto> getLivraisonById(@PathVariable Long id) {
        return livraisonService.getLivraisonById(id)
                .map(livraison -> ResponseEntity.ok(LivraisonDto.convertToDto(livraison)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LivraisonDto createLivraison(@Valid @RequestBody LivraisonDto livraisonDto) {
        Livraison livraison = LivraisonDto.convertToEntity(livraisonDto);
        return LivraisonDto.convertToDto(livraisonService.createLivraison(livraison));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivraisonDto> updateLivraison(@PathVariable Long id, @Valid @RequestBody LivraisonDto livraisonDto) {
        Livraison livraison = LivraisonDto.convertToEntity(livraisonDto);
        return livraisonService.updateLivraison(id, livraison)
                .map(updatedLivraison -> ResponseEntity.ok(LivraisonDto.convertToDto(updatedLivraison)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        return livraisonService.deleteLivraison(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
