package itu.m2.ws.controllers;

import itu.m2.ws.dto.CommandesParJourDto;
import itu.m2.ws.dto.TopRestaurantDto;
import itu.m2.ws.dto.MeilleurClientDto;
import itu.m2.ws.dto.PerformanceLivreurDto;
import itu.m2.ws.dto.RecommandationRestaurantDto;
import itu.m2.ws.repositories.RestaurantRepository;
import itu.m2.ws.repositories.ClientRepository;
import itu.m2.ws.repositories.LivreurRepository;
import itu.m2.ws.repositories.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('ADMIN')")
public class StatsController extends BaseController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private CommandeRepository commandeRepository;

    @GetMapping("/stats/restaurants/top")
    public ResponseEntity<List<TopRestaurantDto>> getTopRestaurants(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        
        Timestamp fromDate = null;
        Timestamp toDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (from != null && !from.isEmpty()) {
            fromDate = Timestamp.valueOf(LocalDate.parse(from, formatter).atStartOfDay());
        }
        if (to != null && !to.isEmpty()) {
            toDate = Timestamp.valueOf(LocalDate.parse(to, formatter).plusDays(1).atStartOfDay().minusNanos(1)); // Fin de la journée
        }

        List<TopRestaurantDto> topRestaurants = restaurantRepository.findTopRestaurants(fromDate, toDate);
        return ResponseEntity.ok(topRestaurants);
    }

    @GetMapping("/stats/clients/meilleurs")
    public ResponseEntity<List<MeilleurClientDto>> getMeilleursClients() {
        List<MeilleurClientDto> meilleursClients = clientRepository.findMeilleursClients();
        return ResponseEntity.ok(meilleursClients);
    }

    @GetMapping("/stats/livreurs/performance")
    public ResponseEntity<List<PerformanceLivreurDto>> getLivreursPerformance() {
        List<PerformanceLivreurDto> performances = livreurRepository.getLivreursPerformance();
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/stats/commandes/par-jour")
    public ResponseEntity<List<CommandesParJourDto>> getCommandesParJour() {
        List<CommandesParJourDto> commandes = commandeRepository.countCommandesByJour();
        return ResponseEntity.ok(commandes);
    }
}
