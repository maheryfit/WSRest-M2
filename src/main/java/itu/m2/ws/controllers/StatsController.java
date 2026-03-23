package itu.m2.ws.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatsController {

    @GetMapping("/stats/restaurants/top")
    public ResponseEntity<List<Object>> getTopRestaurants(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        // Implement logic to return top restaurants
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/stats/clients/meilleurs")
    public ResponseEntity<List<Object>> getMeilleursClients() {
        // Implement logic to return best clients
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/stats/livreurs/performance")
    public ResponseEntity<List<Object>> getLivreursPerformance() {
        // Implement logic to return livreur performance
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/stats/commandes/par-jour")
    public ResponseEntity<List<Object>> getCommandesParJour() {
        // Implement logic to return orders per day
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/recommandations/restaurants")
    public ResponseEntity<List<Object>> getRecommandationsRestaurants() {
        // Implement logic to return recommendations
        return ResponseEntity.ok(List.of());
    }
}
