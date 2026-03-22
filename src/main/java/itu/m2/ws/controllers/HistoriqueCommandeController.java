package itu.m2.ws.controllers;

import itu.m2.ws.dto.HistoriqueCommandeDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.HistoriqueCommande;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.services.HistoriqueCommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/historiquecommandes")
public class HistoriqueCommandeController {

    @Autowired
    private HistoriqueCommandeService historiqueCommandeService;

    private HistoriqueCommandeDto convertToDto(HistoriqueCommande historique) {
        return new HistoriqueCommandeDto(
                historique.getId(),
                historique.getCommande().getId(),
                historique.getDateStatus(),
                historique.getStatutCommande().getId()
        );
    }

    private HistoriqueCommande convertToEntity(HistoriqueCommandeDto historiqueDto) {
        HistoriqueCommande historique = new HistoriqueCommande();
        historique.setDateStatus(historiqueDto.getDateStatus());

        Commande commande = new Commande();
        commande.setId(historiqueDto.getCommandeId());
        historique.setCommande(commande);

        StatutCommande statut = new StatutCommande();
        statut.setId(historiqueDto.getStatutCommandeId());
        historique.setStatutCommande(statut);

        return historique;
    }

    @GetMapping
    public List<HistoriqueCommandeDto> getAllHistoriqueCommandes() {
        return historiqueCommandeService.getAllHistoriqueCommandes().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueCommandeDto> getHistoriqueCommandeById(@PathVariable Long id) {
        return historiqueCommandeService.getHistoriqueCommandeById(id)
                .map(historique -> ResponseEntity.ok(convertToDto(historique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoriqueCommandeDto createHistoriqueCommande(@Valid @RequestBody HistoriqueCommandeDto historiqueDto) {
        HistoriqueCommande historique = convertToEntity(historiqueDto);
        return convertToDto(historiqueCommandeService.createHistoriqueCommande(historique));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueCommandeDto> updateHistoriqueCommande(@PathVariable Long id, @Valid @RequestBody HistoriqueCommandeDto historiqueDto) {
        HistoriqueCommande historique = convertToEntity(historiqueDto);
        return historiqueCommandeService.updateHistoriqueCommande(id, historique)
                .map(updatedHistorique -> ResponseEntity.ok(convertToDto(updatedHistorique)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoriqueCommande(@PathVariable Long id) {
        return historiqueCommandeService.deleteHistoriqueCommande(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
