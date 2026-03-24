package itu.m2.ws.controllers;

import itu.m2.ws.dto.CommandeDto;
import itu.m2.ws.dto.RestaurantDto;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.CommandeService;
import itu.m2.ws.services.RestaurantService;
import itu.m2.ws.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController extends BaseController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CommandeService commandeService;

    @GetMapping
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants().stream().map(RestaurantDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        return restaurantService.getRestaurantById(id)
                .map(restaurant -> ResponseEntity.ok(RestaurantDto.convertToDto(restaurant)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/commandes")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<List<CommandeDto>> getMyCommandes() {
        String email = getCurrentUserEmail();
        // Here we need to find the restaurant associated with the user
        // Assuming restaurantService has a method to find by user email
        // Return mock list for compilation until implementation is added
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/me/commandes/{id}/accepter")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<CommandeDto> accepterCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(2L); // 2: ACCEPTEE_RESTAURANT
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/refuser")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<CommandeDto> refuserCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(0L); // 0: ANNULER
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/en-preparation")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<CommandeDto> enPreparationCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(3L); // 3: EN_PREPARATION
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/pretee")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<CommandeDto> preteeCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(4L); // 4: PRET
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public RestaurantDto createRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        Utilisateur newUser = new Utilisateur();
        newUser.setEmail(restaurantDto.getEmail());
        newUser.setMotDePasseHash(restaurantDto.getMotDePasse()); // Remember to hash in a real app
        newUser.setRole(Role.RESTAURANT);

        Restaurant restaurant = RestaurantDto.convertToEntity(restaurantDto, newUser);
        return RestaurantDto.convertToDto(restaurantService.createRestaurant(restaurant));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT')")
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDto restaurantDto) {
        return restaurantService.getRestaurantById(id)
                .map(existingRestaurant -> {
                    Utilisateur utilisateurToUpdate = existingRestaurant.getUtilisateur();
                    utilisateurToUpdate.setEmail(restaurantDto.getEmail());

                    Restaurant restaurantToUpdate = RestaurantDto.convertToEntity(restaurantDto, utilisateurToUpdate);
                    restaurantToUpdate.setId(id);

                    return restaurantService.updateRestaurant(id, restaurantToUpdate)
                            .map(updatedRestaurant -> ResponseEntity.ok(RestaurantDto.convertToDto(updatedRestaurant)))
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT')")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        return restaurantService.deleteRestaurant(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
