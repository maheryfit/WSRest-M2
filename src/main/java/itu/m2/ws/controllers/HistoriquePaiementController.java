package itu.m2.ws.controllers;

import itu.m2.ws.dto.HistoriquePaiementDto;
import itu.m2.ws.models.HistoriquePaiement;
import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.StatutPaiement;
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

    private HistoriquePaiementDto convertToDto(HistoriquePaiement historique) {
        return new HistoriquePaiementDto(
                historique.getId(),
                historique.getPaiement().getId(),
                historique.getDateStatus(),
                historique.getStatutPaiement().getId()
        );
    }

    private HistoriquePaiement convertToEntity(HistoriquePaiementDto historiqueDto) {
        HistoriquePaiement historique = new HistoriquePaiement();
        historique.setDateStatus(historiqueDto.getDateStatus());

        Paiement paiement = new Paiement();
        paiement.setId(historiqueDto.getPaiementId());
        historique.setPaiement(paiement);

        StatutPaiement statut = new StatutPaiement();
        statut.setId(historiqueDto.getStatutPaiementId());
        historique.setStatutPaiement(statut);

        return historique;
    }

    @GetMapping
    public List<HistoriquePaiementDto> getAllHistoriquePaiements() {
        return historiquePaiementService.getAllHistoriquePaiements().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriquePaiementDto> getHistoriquePaiementById(@PathVariable Long id) {
        return historiquePaiementService.getHistoriquePaiementById(id)
                .map(historique -> ResponseEntity.ok(convertToDto(historique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoriquePaiementDto createHistoriquePaiement(@Valid @RequestBody HistoriquePaiementDto historiqueDto) {
        HistoriquePaiement historique = convertToEntity(historiqueDto);
        return convertToDto(historiquePaiementService.createHistoriquePaiement(historique));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriquePaiementDto> updateHistoriquePaiement(@PathVariable Long id, @Valid @RequestBody HistoriquePaiementDto historiqueDto) {
        HistoriquePaiement historique = convertToEntity(historiqueDto);
        return historiquePaiementService.updateHistoriquePaiement(id, historique)
                .map(updatedHistorique -> ResponseEntity.ok(convertToDto(updatedHistorique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoriquePaiement(@PathVariable Long id) {
        return historiquePaiementService.deleteHistoriquePaiement(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
