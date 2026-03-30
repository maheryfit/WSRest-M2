package itu.m2.ws.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import itu.m2.ws.services.ClientService;
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
public class StatsController extends BaseController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/stats/restaurants/top")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getTopRestaurants(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        return ResponseEntity.ok(statsService.getTopRestaurants(from, to));
    }

    @GetMapping("/stats/clients/meilleurs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getMeilleursClients() {
        return ResponseEntity.ok(statsService.getMeilleursClients());
    }

    @GetMapping("/stats/livreurs/performance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getLivreursPerformance() {
        return ResponseEntity.ok(statsService.getLivreursPerformance());
    }

    @GetMapping("/stats/commandes/par-jour")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getCommandesParJour() {
        return ResponseEntity.ok(statsService.getCommandesParJour());
    }
}
