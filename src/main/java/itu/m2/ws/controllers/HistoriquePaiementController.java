package itu.m2.ws.controllers;

import itu.m2.ws.dto.HistoriquePaiementDto;
import itu.m2.ws.models.HistoriquePaiement;
import itu.m2.ws.services.HistoriquePaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/historiquepaiements")
public class HistoriquePaiementController {

    @Autowired
    private HistoriquePaiementService historiquePaiementService;

    @GetMapping
    public List<HistoriquePaiementDto> getAllHistoriquePaiements() {
        return historiquePaiementService.getAllHistoriquePaiements().stream().map(HistoriquePaiementDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriquePaiementDto> getHistoriquePaiementById(@PathVariable Long id) {
        return historiquePaiementService.getHistoriquePaiementById(id)
                .map(historique -> ResponseEntity.ok(HistoriquePaiementDto.convertToDto(historique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoriquePaiementDto createHistoriquePaiement(@Valid @RequestBody HistoriquePaiementDto historiqueDto) {
        HistoriquePaiement historique = HistoriquePaiementDto.convertToEntity(historiqueDto);
        return HistoriquePaiementDto.convertToDto(historiquePaiementService.createHistoriquePaiement(historique));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriquePaiementDto> updateHistoriquePaiement(@PathVariable Long id, @Valid @RequestBody HistoriquePaiementDto historiqueDto) {
        HistoriquePaiement historique = HistoriquePaiementDto.convertToEntity(historiqueDto);
        return historiquePaiementService.updateHistoriquePaiement(id, historique)
                .map(updatedHistorique -> ResponseEntity.ok(HistoriquePaiementDto.convertToDto(updatedHistorique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoriquePaiement(@PathVariable Long id) {
        return historiquePaiementService.deleteHistoriquePaiement(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
