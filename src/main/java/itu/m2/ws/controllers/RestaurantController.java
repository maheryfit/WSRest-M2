package itu.m2.ws.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import itu.m2.ws.dto.CommandeDto;
import itu.m2.ws.dto.RestaurantDto;
import itu.m2.ws.enums.Role;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.CommandeService;
import itu.m2.ws.services.RestaurantService;
import jakarta.validation.Valid;

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
    @PreAuthorize("hasAnyRole('RESTAURANT')")
    public ResponseEntity<List<CommandeDto>> getMyCommandes() {
        String email = getCurrentUserEmail();
        
        return restaurantService.getRestaurantByEmail(email)
                .map(restaurant -> {
                    List<CommandeDto> commandes = commandeService.getCommandesByRestaurantId(restaurant.getId())
                            .stream()
                            .map(CommandeDto::convertToDto)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(commandes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/accepter")
    @PreAuthorize("hasAnyRole('RESTAURANT')")
    public ResponseEntity<CommandeDto> accepterCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(2L); // 2: ACCEPTEE_RESTAURANT
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/refuser")
    @PreAuthorize("hasAnyRole('RESTAURANT')")
    public ResponseEntity<CommandeDto> refuserCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(0L); // 0: ANNULER
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/en-preparation")
    @PreAuthorize("hasAnyRole('RESTAURANT')")
    public ResponseEntity<CommandeDto> enPreparationCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(3L); // 3: EN_PREPARATION
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(CommandeDto.convertToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/pretee")
    @PreAuthorize("hasAnyRole('RESTAURANT')")
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
