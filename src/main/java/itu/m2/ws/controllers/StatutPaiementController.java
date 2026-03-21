package itu.m2.ws.controllers;

import itu.m2.ws.dto.StatutPaiementDto;
import itu.m2.ws.models.StatutPaiement;
import itu.m2.ws.services.StatutPaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statutpaiements")
public class StatutPaiementController {

    @Autowired
    private StatutPaiementService statutPaiementService;

    private StatutPaiementDto convertToDto(StatutPaiement statut) {
        return new StatutPaiementDto(statut.getId(), statut.getLibelle(), statut.getRang());
    }

    private StatutPaiement convertToEntity(StatutPaiementDto statutDto) {
        StatutPaiement statut = new StatutPaiement();
        statut.setLibelle(statutDto.getLibelle());
        statut.setRang(statutDto.getRang());
        return statut;
    }

    @GetMapping
    public List<StatutPaiementDto> getAllStatutPaiements() {
        return statutPaiementService.getAllStatutPaiements().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatutPaiementDto> getStatutPaiementById(@PathVariable Long id) {
        return statutPaiementService.getStatutPaiementById(id)
                .map(statut -> ResponseEntity.ok(convertToDto(statut)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public StatutPaiementDto createStatutPaiement(@Valid @RequestBody StatutPaiementDto statutDto) {
        StatutPaiement statut = convertToEntity(statutDto);
        return convertToDto(statutPaiementService.createStatutPaiement(statut));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatutPaiementDto> updateStatutPaiement(@PathVariable Long id, @Valid @RequestBody StatutPaiementDto statutDto) {
        StatutPaiement statut = convertToEntity(statutDto);
        return statutPaiementService.updateStatutPaiement(id, statut)
                .map(updatedStatut -> ResponseEntity.ok(convertToDto(updatedStatut)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatutPaiement(@PathVariable Long id) {
        return statutPaiementService.deleteStatutPaiement(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
