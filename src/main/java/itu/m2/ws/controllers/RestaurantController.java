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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CommandeService commandeService;

    private RestaurantDto convertToDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setNom(restaurant.getNom());
        dto.setDescription(restaurant.getDescription());
        dto.setTelephone(restaurant.getTelephone());
        dto.setAdresse(restaurant.getAdresse());
        dto.setVille(restaurant.getVille());
        dto.setLatitude(restaurant.getLatitude());
        dto.setLongitude(restaurant.getLongitude());
        dto.setOuvert(restaurant.isOuvert());
        dto.setNoteMoyenne(restaurant.getNoteMoyenne());
        dto.setEmail(restaurant.getUtilisateur().getEmail());
        return dto;
    }

    private CommandeDto convertCommandeToDto(itu.m2.ws.models.Commande commande) {
        return new CommandeDto(
                commande.getId(),
                commande.getClient().getId(),
                commande.getRestaurant().getId(),
                commande.getStatutCommande().getId(),
                commande.getMontantTotal(),
                commande.getModePaiement(),
                commande.getDateCreation()
        );
    }

    private Restaurant convertToEntity(RestaurantDto restaurantDto, Utilisateur utilisateur) {
        Restaurant restaurant = new Restaurant();
        restaurant.setNom(restaurantDto.getNom());
        restaurant.setDescription(restaurantDto.getDescription());
        restaurant.setTelephone(restaurantDto.getTelephone());
        restaurant.setAdresse(restaurantDto.getAdresse());
        restaurant.setVille(restaurantDto.getVille());
        restaurant.setLatitude(restaurantDto.getLatitude());
        restaurant.setLongitude(restaurantDto.getLongitude());
        restaurant.setOuvert(restaurantDto.isOuvert());
        restaurant.setNoteMoyenne(restaurantDto.getNoteMoyenne());
        restaurant.setUtilisateur(utilisateur);
        return restaurant;
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            assert principal != null;
            return principal.toString();
        }
    }

    @GetMapping
    public List<RestaurantDto> getAllRestaurants() {
        return restaurantService.getAllRestaurants().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        return restaurantService.getRestaurantById(id)
                .map(restaurant -> ResponseEntity.ok(convertToDto(restaurant)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/commandes")
    public ResponseEntity<List<CommandeDto>> getMyCommandes() {
        String email = getCurrentUserEmail();
        // Here we need to find the restaurant associated with the user
        // Assuming restaurantService has a method to find by user email
        // Return mock list for compilation until implementation is added
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/me/commandes/{id}/accepter")
    public ResponseEntity<CommandeDto> accepterCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(2L); // 2: ACCEPTEE_RESTAURANT
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(convertCommandeToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/refuser")
    public ResponseEntity<CommandeDto> refuserCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(0L); // 0: ANNULER
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(convertCommandeToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/en-preparation")
    public ResponseEntity<CommandeDto> enPreparationCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(3L); // 3: EN_PREPARATION
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(convertCommandeToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/commandes/{id}/pretee")
    public ResponseEntity<CommandeDto> preteeCommande(@PathVariable Long id) {
        StatutCommande statut = new StatutCommande();
        statut.setId(4L); // 4: PRET
        return commandeService.updateStatutCommande(id, statut)
                .map(updatedCommande -> ResponseEntity.ok(convertCommandeToDto(updatedCommande)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RestaurantDto createRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        Utilisateur newUser = new Utilisateur();
        newUser.setEmail(restaurantDto.getEmail());
        newUser.setMotDePasseHash(restaurantDto.getMotDePasse()); // Remember to hash in a real app
        newUser.setRole(Role.RESTAURANT);

        Restaurant restaurant = convertToEntity(restaurantDto, newUser);
        return convertToDto(restaurantService.createRestaurant(restaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDto restaurantDto) {
        return restaurantService.getRestaurantById(id)
                .map(existingRestaurant -> {
                    Utilisateur utilisateurToUpdate = existingRestaurant.getUtilisateur();
                    utilisateurToUpdate.setEmail(restaurantDto.getEmail());

                    Restaurant restaurantToUpdate = convertToEntity(restaurantDto, utilisateurToUpdate);
                    restaurantToUpdate.setId(id);

                    return restaurantService.updateRestaurant(id, restaurantToUpdate)
                            .map(updatedRestaurant -> ResponseEntity.ok(convertToDto(updatedRestaurant)))
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        return restaurantService.deleteRestaurant(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
