package itu.m2.ws.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import itu.m2.ws.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "ADMIN", description = "Endpoints réservés à l'administration")
@SecurityRequirement(name = "bearerAuth")
public class StatsController extends BaseController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/stats/restaurants/top")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Top Restaurants", description = "Retourne les restaurants les plus populaires (nombre de commandes, note moyenne, CA) sur une période donnée.")
    public ResponseEntity<List<Map<String, Object>>> getTopRestaurants(
            @Parameter(description = "Date de début (YYYY-MM-DD)") @RequestParam(required = false) String from,
            @Parameter(description = "Date de fin (YYYY-MM-DD)") @RequestParam(required = false) String to) {
        return ResponseEntity.ok(statsService.getTopRestaurants(from, to));
    }

    @GetMapping("/stats/clients/meilleurs")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Meilleurs Clients", description = "Liste des meilleurs clients : total dépensé, nombre de commandes, panier moyen.")
    public ResponseEntity<List<Map<String, Object>>> getMeilleursClients() {
        return ResponseEntity.ok(statsService.getMeilleursClients());
    }

    @GetMapping("/stats/commandes/par-jour")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Commandes par Jour", description = "Nombre de commandes par jour sur une période.")
    public ResponseEntity<List<Map<String, Object>>> getCommandesParJour() {
        return ResponseEntity.ok(statsService.getCommandesParJour());
    }
}
