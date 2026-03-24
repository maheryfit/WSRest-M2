package itu.m2.ws.controllers;

import itu.m2.ws.dto.HistoriqueLivraisonDto;
import itu.m2.ws.models.HistoriqueLivraison;
import itu.m2.ws.services.HistoriqueLivraisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/historiquelivraisons")
public class HistoriqueLivraisonController {

    @Autowired
    private HistoriqueLivraisonService historiqueLivraisonService;

    @GetMapping
    public List<HistoriqueLivraisonDto> getAllHistoriqueLivraisons() {
        return historiqueLivraisonService.getAllHistoriqueLivraisons().stream().map(HistoriqueLivraisonDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueLivraisonDto> getHistoriqueLivraisonById(@PathVariable Long id) {
        return historiqueLivraisonService.getHistoriqueLivraisonById(id)
                .map(historique -> ResponseEntity.ok(HistoriqueLivraisonDto.convertToDto(historique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoriqueLivraisonDto createHistoriqueLivraison(@Valid @RequestBody HistoriqueLivraisonDto historiqueDto) {
        HistoriqueLivraison historique = HistoriqueLivraisonDto.convertToEntity(historiqueDto);
        return HistoriqueLivraisonDto.convertToDto(historiqueLivraisonService.createHistoriqueLivraison(historique));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueLivraisonDto> updateHistoriqueLivraison(@PathVariable Long id, @Valid @RequestBody HistoriqueLivraisonDto historiqueDto) {
        HistoriqueLivraison historique = HistoriqueLivraisonDto.convertToEntity(historiqueDto);
        return historiqueLivraisonService.updateHistoriqueLivraison(id, historique)
                .map(updatedHistorique -> ResponseEntity.ok(HistoriqueLivraisonDto.convertToDto(updatedHistorique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoriqueLivraison(@PathVariable Long id) {
        return historiqueLivraisonService.deleteHistoriqueLivraison(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
