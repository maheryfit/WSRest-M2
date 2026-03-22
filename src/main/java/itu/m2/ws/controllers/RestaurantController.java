package itu.m2.ws.controllers;

import itu.m2.ws.dto.RestaurantDto;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    private RestaurantDto convertToDto(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getUtilisateur().getId(),
                restaurant.getNom(),
                restaurant.getDescription(),
                restaurant.getTelephone(),
                restaurant.getAdresse(),
                restaurant.getVille(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.isOuvert(),
                restaurant.getNoteMoyenne()
        );
    }

    private Restaurant convertToEntity(RestaurantDto restaurantDto) {
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

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(restaurantDto.getUtilisateurId());
        restaurant.setUtilisateur(utilisateur);

        return restaurant;
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

    @PostMapping
    public RestaurantDto createRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        Restaurant restaurant = convertToEntity(restaurantDto);
        return convertToDto(restaurantService.createRestaurant(restaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDto restaurantDto) {
        Restaurant restaurant = convertToEntity(restaurantDto);
        return restaurantService.updateRestaurant(id, restaurant)
                .map(updatedRestaurant -> ResponseEntity.ok(convertToDto(updatedRestaurant)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        return restaurantService.deleteRestaurant(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
