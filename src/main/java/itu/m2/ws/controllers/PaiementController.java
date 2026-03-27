package itu.m2.ws.controllers;

import itu.m2.ws.dto.PaiementDto;
import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.StatutPaiement;
import itu.m2.ws.services.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @GetMapping("/paiements")
    public List<PaiementDto> getAllPaiements() {
        return paiementService.getAllPaiements().stream().map(PaiementDto::convertToDto).collect(Collectors.toList());
    }
    
    @GetMapping("/commandes/{commandeId}/paiement")
    @PreAuthorize("hasAnyRole('CLIENT', 'RESTAURANT', 'ADMIN')")
    public ResponseEntity<PaiementDto> getPaiementByCommandeId(@PathVariable Long commandeId) {
        return paiementService.getPaiementByCommandeId(commandeId)
                .map(paiement -> ResponseEntity.ok(PaiementDto.convertToDto(paiement)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/commandes/{commandeId}/paiement")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<PaiementDto> initierPaiement(@PathVariable Long commandeId, @RequestBody PaiementDto paiementDto) {
        // Initier un paiement
        if (paiementDto.getStatutPaiementId() == null) {
            paiementDto.setStatutPaiementId(1L); // 1 = En attente
        }
        paiementDto.setCommandeId(commandeId);
        
        Paiement paiement = PaiementDto.convertToEntity(paiementDto);
        Paiement savedPaiement = paiementService.createPaiement(paiement);
        
        return ResponseEntity.ok(PaiementDto.convertToDto(savedPaiement));
    }
    
    @PostMapping("/paiements/{id}/confirmer")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM')")
    public ResponseEntity<PaiementDto> confirmerPaiement(@PathVariable Long id) {
        StatutPaiement statutSucces = new StatutPaiement();
        statutSucces.setId(2L); // 2 = Succès
        
        return paiementService.updateStatutPaiement(id, statutSucces)
                .map(updatedPaiement -> ResponseEntity.ok(PaiementDto.convertToDto(updatedPaiement)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/paiements/{id}/echouer")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM')")
    public ResponseEntity<PaiementDto> echouerPaiement(@PathVariable Long id) {
        StatutPaiement statutEchec = new StatutPaiement();
        statutEchec.setId(0L); // 0 = Échec ou 3, à ajuster selon la BDD
        
        return paiementService.updateStatutPaiement(id, statutEchec)
                .map(updatedPaiement -> ResponseEntity.ok(PaiementDto.convertToDto(updatedPaiement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paiements/{id}")
    public ResponseEntity<PaiementDto> getPaiementById(@PathVariable Long id) {
        return paiementService.getPaiementById(id)
                .map(paiement -> ResponseEntity.ok(PaiementDto.convertToDto(paiement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/paiements")
    public PaiementDto createPaiement(@Valid @RequestBody PaiementDto paiementDto) {
        Paiement paiement = PaiementDto.convertToEntity(paiementDto);
        return PaiementDto.convertToDto(paiementService.createPaiement(paiement));
    }

    @PutMapping("/paiements/{id}")
    public ResponseEntity<PaiementDto> updatePaiement(@PathVariable Long id, @Valid @RequestBody PaiementDto paiementDto) {
        Paiement paiement = PaiementDto.convertToEntity(paiementDto);
        return paiementService.updatePaiement(id, paiement)
                .map(updatedPaiement -> ResponseEntity.ok(PaiementDto.convertToDto(updatedPaiement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/paiements/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        return paiementService.deletePaiement(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
