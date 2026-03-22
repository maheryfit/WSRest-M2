package itu.m2.ws.controllers;

import itu.m2.ws.dto.StatutCommandeDto;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.services.StatutCommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statutcommandes")
public class StatutCommandeController {

    @Autowired
    private StatutCommandeService statutCommandeService;

    private StatutCommandeDto convertToDto(StatutCommande statut) {
        return new StatutCommandeDto(statut.getId(), statut.getLibelle(), statut.getRang());
    }

    private StatutCommande convertToEntity(StatutCommandeDto statutDto) {
        StatutCommande statut = new StatutCommande();
        statut.setLibelle(statutDto.getLibelle());
        statut.setRang(statutDto.getRang());
        return statut;
    }

    @GetMapping
    public List<StatutCommandeDto> getAllStatutCommandes() {
        return statutCommandeService.getAllStatutCommandes().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatutCommandeDto> getStatutCommandeById(@PathVariable Long id) {
        return statutCommandeService.getStatutCommandeById(id)
                .map(statut -> ResponseEntity.ok(convertToDto(statut)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public StatutCommandeDto createStatutCommande(@Valid @RequestBody StatutCommandeDto statutDto) {
        StatutCommande statut = convertToEntity(statutDto);
        return convertToDto(statutCommandeService.createStatutCommande(statut));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatutCommandeDto> updateStatutCommande(@PathVariable Long id, @Valid @RequestBody StatutCommandeDto statutDto) {
        StatutCommande statut = convertToEntity(statutDto);
        return statutCommandeService.updateStatutCommande(id, statut)
                .map(updatedStatut -> ResponseEntity.ok(convertToDto(updatedStatut)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatutCommande(@PathVariable Long id) {
        return statutCommandeService.deleteStatutCommande(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
