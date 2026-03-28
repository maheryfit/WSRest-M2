package itu.m2.ws.controllers;

import itu.m2.ws.services.ClientService;
import itu.m2.ws.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatsController extends BaseController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/stats/restaurants/top")
    public ResponseEntity<List<Map<String, Object>>> getTopRestaurants(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        return ResponseEntity.ok(statsService.getTopRestaurants(from, to));
    }

    @GetMapping("/stats/clients/meilleurs")
    public ResponseEntity<List<Map<String, Object>>> getMeilleursClients() {
        return ResponseEntity.ok(statsService.getMeilleursClients());
    }

    @GetMapping("/stats/livreurs/performance")
    public ResponseEntity<List<Map<String, Object>>> getLivreursPerformance() {
        return ResponseEntity.ok(statsService.getLivreursPerformance());
    }

    @GetMapping("/stats/commandes/par-jour")
    public ResponseEntity<List<Map<String, Object>>> getCommandesParJour() {
        return ResponseEntity.ok(statsService.getCommandesParJour());
    }

    @GetMapping("/recommandations/restaurants")
    public ResponseEntity<List<Map<String, Object>>> getRecommandationsRestaurants() {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> ResponseEntity.ok(statsService.getRecommandationsRestaurants(client.getId())))
                .orElse(ResponseEntity.notFound().build());
    }
}
