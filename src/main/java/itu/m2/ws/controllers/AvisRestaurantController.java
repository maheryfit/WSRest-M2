package itu.m2.ws.controllers;

import itu.m2.ws.dto.AvisRestaurantDto;
import itu.m2.ws.models.AvisRestaurant;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.services.AvisRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AvisRestaurantController {

    @Autowired
    private AvisRestaurantService avisRestaurantService;

    private AvisRestaurantDto convertToDto(AvisRestaurant avis) {
        return new AvisRestaurantDto(
                avis.getId(),
                avis.getRestaurant().getId(),
                avis.getClient().getId(),
                avis.getNote(),
                avis.getCommentaire(),
                avis.getDateCreation()
        );
    }

    private AvisRestaurant convertToEntity(AvisRestaurantDto avisDto, Long restaurantId) {
        AvisRestaurant avis = new AvisRestaurant();
        avis.setNote(avisDto.getNote());
        avis.setCommentaire(avisDto.getCommentaire());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        avis.setRestaurant(restaurant);

        Client client = new Client();
        client.setId(avisDto.getClientId());
        avis.setClient(client);

        return avis;
    }

    @GetMapping("/avisrestaurants")
    public List<AvisRestaurantDto> getAllAvisRestaurants() {
        return avisRestaurantService.getAllAvisRestaurants().stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @GetMapping("/restaurants/{restaurantId}/avis")
    public ResponseEntity<List<AvisRestaurantDto>> getAvisByRestaurantId(@PathVariable Long restaurantId) {
        // Needs findByRestaurantId in Service
        return ResponseEntity.ok(List.of());
    }
    
    @PostMapping("/restaurants/{restaurantId}/avis")
    public ResponseEntity<AvisRestaurantDto> createAvisByRestaurantId(@PathVariable Long restaurantId, @Valid @RequestBody AvisRestaurantDto avisDto) {
        // Should verify if user has ordered from here before
        AvisRestaurant avis = convertToEntity(avisDto, restaurantId);
        return ResponseEntity.ok(convertToDto(avisRestaurantService.createAvisRestaurant(avis)));
    }
    
    @PutMapping("/restaurants/{restaurantId}/avis/{id}")
    public ResponseEntity<AvisRestaurantDto> updateAvisByRestaurantId(@PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody AvisRestaurantDto avisDto) {
        AvisRestaurant avis = convertToEntity(avisDto, restaurantId);
        return avisRestaurantService.updateAvisRestaurant(id, avis)
                .map(updatedAvis -> ResponseEntity.ok(convertToDto(updatedAvis)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/restaurants/{restaurantId}/avis/{id}")
    public ResponseEntity<Void> deleteAvisByRestaurantId(@PathVariable Long restaurantId, @PathVariable Long id) {
        return avisRestaurantService.deleteAvisRestaurant(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/avisrestaurants/{id}")
    public ResponseEntity<AvisRestaurantDto> getAvisRestaurantById(@PathVariable Long id) {
        return avisRestaurantService.getAvisRestaurantById(id)
                .map(avis -> ResponseEntity.ok(convertToDto(avis)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/avisrestaurants")
    public AvisRestaurantDto createAvisRestaurant(@Valid @RequestBody AvisRestaurantDto avisDto) {
        AvisRestaurant avis = convertToEntity(avisDto, avisDto.getRestaurantId());
        return convertToDto(avisRestaurantService.createAvisRestaurant(avis));
    }

    @PutMapping("/avisrestaurants/{id}")
    public ResponseEntity<AvisRestaurantDto> updateAvisRestaurant(@PathVariable Long id, @Valid @RequestBody AvisRestaurantDto avisDto) {
        AvisRestaurant avis = convertToEntity(avisDto, avisDto.getRestaurantId());
        return avisRestaurantService.updateAvisRestaurant(id, avis)
                .map(updatedAvis -> ResponseEntity.ok(convertToDto(updatedAvis)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/avisrestaurants/{id}")
    public ResponseEntity<Void> deleteAvisRestaurant(@PathVariable Long id) {
        return avisRestaurantService.deleteAvisRestaurant(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
