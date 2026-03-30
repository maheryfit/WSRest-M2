package itu.m2.ws.controllers;

import itu.m2.ws.dto.PaiementDto;
import itu.m2.ws.models.Paiement;
import itu.m2.ws.services.PaiementService;
import itu.m2.ws.services.StatutPaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @Autowired
    private StatutPaiementService statutPaiementService;

    @GetMapping("/paiements")
    public List<PaiementDto> getAllPaiements() {
        return paiementService.getAllPaiements().stream().map(PaiementDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/commandes/{commandeId}/paiements")
    public ResponseEntity<List<PaiementDto>> getPaiementsByCommandeId(@PathVariable Long commandeId) {
        List<PaiementDto> paiements = this.paiementService.getPaiementsByCommandeId(commandeId)
                .stream().map(PaiementDto::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(paiements);
    }

    @PostMapping("/commandes/{commandeId}/paiement")
    public ResponseEntity<?> initierPaiement(@PathVariable Long commandeId,
            @RequestBody PaiementDto paiementDto) {
        paiementDto.setCommandeId(commandeId);
        if (paiementDto.getStatutPaiementId() == null) {
            statutPaiementService.getStatutPaiementByLibelle("INITIALISE")
                    .ifPresent(s -> paiementDto.setStatutPaiementId(s.getId()));
        }
        try {
            Paiement paiement = PaiementDto.convertToEntity(paiementDto);
            return ResponseEntity.ok(PaiementDto.convertToDto(paiementService.createPaiement(paiement)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/paiements/{id}/confirmer")
    public ResponseEntity<PaiementDto> confirmerPaiement(@PathVariable Long id) {
        return paiementService.updateStatutPaiement(id, "SUCCES")
                .map(p -> ResponseEntity.ok(PaiementDto.convertToDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/paiements/{id}/echouer")
    public ResponseEntity<PaiementDto> echouerPaiement(@PathVariable Long id) {
        return paiementService.updateStatutPaiement(id, "ECHEC")
                .map(p -> ResponseEntity.ok(PaiementDto.convertToDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paiements/{id}")
    public ResponseEntity<PaiementDto> getPaiementById(@PathVariable Long id) {
        return paiementService.getPaiementById(id)
                .map(paiement -> ResponseEntity.ok(PaiementDto.convertToDto(paiement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/paiements")
    public ResponseEntity<?> createPaiement(@Valid @RequestBody PaiementDto paiementDto) {
        try {
            Paiement paiement = PaiementDto.convertToEntity(paiementDto);
            return ResponseEntity.ok(PaiementDto.convertToDto(paiementService.createPaiement(paiement)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/paiements/{id}")
    public ResponseEntity<PaiementDto> updatePaiement(@PathVariable Long id,
            @Valid @RequestBody PaiementDto paiementDto) {
        Paiement paiement = PaiementDto.convertToEntity(paiementDto);
        return paiementService.updatePaiement(id, paiement)
                .map(updatedPaiement -> ResponseEntity.ok(PaiementDto.convertToDto(updatedPaiement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/paiements/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        return paiementService.deletePaiement(id) ? ResponseEntity.ok().<Void>build()
                : ResponseEntity.notFound().<Void>build();
    }
}
