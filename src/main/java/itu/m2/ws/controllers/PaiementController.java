package itu.m2.ws.controllers;

import itu.m2.ws.dto.PaiementDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.StatutPaiement;
import itu.m2.ws.services.PaiementService;
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

    private PaiementDto convertToDto(Paiement paiement) {
        return new PaiementDto(
                paiement.getId(),
                paiement.getCommande().getId(),
                paiement.getMontant(),
                paiement.getStatutPaiement().getId(),
                paiement.getDatePaiement()
        );
    }

    private Paiement convertToEntity(PaiementDto paiementDto) {
        Paiement paiement = new Paiement();
        paiement.setMontant(paiementDto.getMontant());
        paiement.setDatePaiement(paiementDto.getDatePaiement());

        Commande commande = new Commande();
        commande.setId(paiementDto.getCommandeId());
        paiement.setCommande(commande);

        StatutPaiement statutPaiement = new StatutPaiement();
        statutPaiement.setId(paiementDto.getStatutPaiementId());
        paiement.setStatutPaiement(statutPaiement);

        return paiement;
    }

    @GetMapping("/paiements")
    public List<PaiementDto> getAllPaiements() {
        return paiementService.getAllPaiements().stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @GetMapping("/commandes/{commandeId}/paiement")
    public ResponseEntity<PaiementDto> getPaiementByCommandeId(@PathVariable Long commandeId) {
        // Needs findByCommandeId in PaiementRepository/Service
        // Return mock for now
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/commandes/{commandeId}/paiement")
    public ResponseEntity<PaiementDto> initierPaiement(@PathVariable Long commandeId, @RequestBody PaiementDto paiementDto) {
        // Initier un paiement
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/paiements/{id}/confirmer")
    public ResponseEntity<PaiementDto> confirmerPaiement(@PathVariable Long id) {
        // Update statut to SUCCES
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/paiements/{id}/echouer")
    public ResponseEntity<PaiementDto> echouerPaiement(@PathVariable Long id) {
        // Update statut to ECHEC
        return ResponseEntity.ok().build();
    }

    @GetMapping("/paiements/{id}")
    public ResponseEntity<PaiementDto> getPaiementById(@PathVariable Long id) {
        return paiementService.getPaiementById(id)
                .map(paiement -> ResponseEntity.ok(convertToDto(paiement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/paiements")
    public PaiementDto createPaiement(@Valid @RequestBody PaiementDto paiementDto) {
        Paiement paiement = convertToEntity(paiementDto);
        return convertToDto(paiementService.createPaiement(paiement));
    }

    @PutMapping("/paiements/{id}")
    public ResponseEntity<PaiementDto> updatePaiement(@PathVariable Long id, @Valid @RequestBody PaiementDto paiementDto) {
        Paiement paiement = convertToEntity(paiementDto);
        return paiementService.updatePaiement(id, paiement)
                .map(updatedPaiement -> ResponseEntity.ok(convertToDto(updatedPaiement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/paiements/{id}")
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        return paiementService.deletePaiement(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
