package itu.m2.ws.controllers;

import itu.m2.ws.dto.HistoriqueLivraisonDto;
import itu.m2.ws.models.HistoriqueLivraison;
import itu.m2.ws.models.Livraison;
import itu.m2.ws.models.StatutLivraison;
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

    private HistoriqueLivraisonDto convertToDto(HistoriqueLivraison historique) {
        return new HistoriqueLivraisonDto(
                historique.getId(),
                historique.getLivraison().getId(),
                historique.getDateStatus(),
                historique.getStatutLivraison().getId()
        );
    }

    private HistoriqueLivraison convertToEntity(HistoriqueLivraisonDto historiqueDto) {
        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setDateStatus(historiqueDto.getDateStatus());

        Livraison livraison = new Livraison();
        livraison.setId(historiqueDto.getLivraisonId());
        historique.setLivraison(livraison);

        StatutLivraison statut = new StatutLivraison();
        statut.setId(historiqueDto.getStatutLivraisonId());
        historique.setStatutLivraison(statut);

        return historique;
    }

    @GetMapping
    public List<HistoriqueLivraisonDto> getAllHistoriqueLivraisons() {
        return historiqueLivraisonService.getAllHistoriqueLivraisons().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueLivraisonDto> getHistoriqueLivraisonById(@PathVariable Long id) {
        return historiqueLivraisonService.getHistoriqueLivraisonById(id)
                .map(historique -> ResponseEntity.ok(convertToDto(historique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoriqueLivraisonDto createHistoriqueLivraison(@Valid @RequestBody HistoriqueLivraisonDto historiqueDto) {
        HistoriqueLivraison historique = convertToEntity(historiqueDto);
        return convertToDto(historiqueLivraisonService.createHistoriqueLivraison(historique));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueLivraisonDto> updateHistoriqueLivraison(@PathVariable Long id, @Valid @RequestBody HistoriqueLivraisonDto historiqueDto) {
        HistoriqueLivraison historique = convertToEntity(historiqueDto);
        return historiqueLivraisonService.updateHistoriqueLivraison(id, historique)
                .map(updatedHistorique -> ResponseEntity.ok(convertToDto(updatedHistorique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoriqueLivraison(@PathVariable Long id) {
        return historiqueLivraisonService.deleteHistoriqueLivraison(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
